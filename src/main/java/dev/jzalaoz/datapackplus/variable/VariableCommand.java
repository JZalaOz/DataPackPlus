package dev.jzalaoz.datapackplus.variable;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.jzalaoz.datapackplus.DataPackPlusMain;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class VariableCommand {

    public static final Collection<Pair<String, Message>> VARIABLE_SUGGESTIONS = VariableType.getSuggestions();
    public static final SuggestionProvider<ServerCommandSource> VARIABLE_SUGGESTION_PROVIDER = (context, builder) -> {
        String input = builder.getRemaining().toLowerCase(Locale.ROOT);

        for (Pair<String, Message> suggestion : VARIABLE_SUGGESTIONS) {
            if (suggestion.getLeft().toLowerCase().startsWith(input)) {
                builder.suggest(suggestion.getLeft(), suggestion.getRight());
            }
        }

        return builder.buildFuture();
    };

    public static void register(LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder) {
        // Ik this is very messy, but I am lazy, if someone else wants to fix go ahead

        Consumer<Pair<RequiredArgumentBuilder<ServerCommandSource, String>, Pair<Pair<String, Boolean>, Command<ServerCommandSource>>>> inputAccessor = (pair) -> {
            pair.getLeft().then(CommandManager.literal(pair.getRight().getLeft().getLeft())
                            .then(CommandManager.argument("identifier", IdentifierArgumentType.identifier())
                                    .suggests(DataPackPlusMain.STORAGE_SUGGESTION_PROVIDER)
                                    .executes(pair.getRight().getRight())
                                    .then(CommandManager.argument("path", NbtPathArgumentType.nbtPath())
                                            .executes(pair.getRight().getRight())
                                    )
                            )
                    );

            if (pair.getRight().getLeft().getRight()) {
                pair.getLeft().then(CommandManager.literal("with")
                        .then(CommandManager.argument("nbt", NbtCompoundArgumentType.nbtCompound())
                                .executes(pair.getRight().getRight())
                        )
                );
            }
        };

        LiteralArgumentBuilder<ServerCommandSource> variable = CommandManager.literal("variable");

        // Get
        {
            var variableArgument = CommandManager.argument("variable", StringArgumentType.word())
                    .suggests(VARIABLE_SUGGESTION_PROVIDER)
                    .executes(context -> executeGet(
                            context,
                            VariableType.getFromKey(StringArgumentType.getString(context, "variable"))
                    ));

            inputAccessor.accept(new Pair<>(variableArgument, new Pair<>(new Pair<>("to", false), context ->
                    executeGet(context, VariableType.getFromKey(StringArgumentType.getString(context, "variable")))
            )));

            variable.then(CommandManager.literal("get").then(variableArgument));
        }

        // Set
        {
            var variableArgument = CommandManager.argument("variable", StringArgumentType.word())
                    .suggests(VARIABLE_SUGGESTION_PROVIDER)
                    .executes(context -> executeGet(
                            context,
                            VariableType.getFromKey(StringArgumentType.getString(context, "variable"))
                    ));

            inputAccessor.accept(new Pair<>(variableArgument, new Pair<>(new Pair<>("from", true), context ->
                    executeUpdate(context, VariableType.getFromKey(StringArgumentType.getString(context, "variable")), false)
            )));

            variable.then(CommandManager.literal("set").then(variableArgument));
        }

        // Add
        {
            var variableArgument = CommandManager.argument("variable", StringArgumentType.word())
                    .suggests(VARIABLE_SUGGESTION_PROVIDER)
                    .executes(context -> executeGet(
                            context,
                            VariableType.getFromKey(StringArgumentType.getString(context, "variable"))
                    ));

            inputAccessor.accept(new Pair<>(variableArgument, new Pair<>(new Pair<>("from", true), context ->
                    executeUpdate(context, VariableType.getFromKey(StringArgumentType.getString(context, "variable")), true)
            )));

            variable.then(CommandManager.literal("add").then(variableArgument));
        }

        literalArgumentBuilder.then(variable);
    }

    private static int executeUpdate(
            CommandContext<ServerCommandSource> context,
            VariableType type,
            boolean add
    ) throws CommandSyntaxException {
        try {
            Pair<Identifier, NbtPathArgumentType.@Nullable NbtPath> storageData = getStorageNbtArgument(context);
            NbtCompound root = null;
            NbtCompound nbt = null;

            if (storageData == null) {
                nbt = NbtCompoundArgumentType.getNbtCompound(context, "nbt");
            } else {
                Pair<NbtCompound, NbtCompound> data = getRootAndTargetCompounds(context, storageData);
                root = data.getLeft();
                nbt = data.getRight();
            }

            NbtList output = processVariable(context, type, nbt.getListOrEmpty("in"), add ? VariableTypeModifier.ADD : VariableTypeModifier.SET);
            nbt.put("out", output);

            if (storageData == null) {
                context.getSource().sendMessage(Text.literal("Successfully updated variable: " + output));
            } else {
                assert root != null;
                context.getSource().getServer().getDataCommandStorage().set(storageData.getLeft(), root);
                context.getSource().sendMessage(Text.literal("Successfully updated variable."));
            }
        } catch (Exception exception) {
            handleException(exception);
        }
        return 0;
    }

    private static int executeGet(
            CommandContext<ServerCommandSource> context,
            VariableType type
    ) throws CommandSyntaxException {
        try {
            Pair<Identifier, NbtPathArgumentType.@Nullable NbtPath> storageData = getStorageNbtArgument(context);
            NbtList output = processVariable(context, type, new NbtList(), VariableTypeModifier.GET);

            if (storageData != null) {
                Pair<NbtCompound, NbtCompound> nbt = getRootAndTargetCompounds(context, storageData);
                nbt.getRight().put("out", output);
                context.getSource().getServer().getDataCommandStorage().set(storageData.getLeft(), nbt.getLeft());
            }

            context.getSource().sendMessage(Text.literal("Success " + output));
        } catch (Exception exception) {
            handleException(exception);
        }
        return 0;
    }

    private static NbtList processVariable(CommandContext<ServerCommandSource> context, VariableType type, NbtList input, VariableTypeModifier modifier) throws CommandSyntaxException {
        NbtList output = new NbtList();
        type.handler.evaluate(input, output, context.getSource(), modifier);
        return output;
    }

    private static void handleException(Exception exception) throws CommandSyntaxException {
        if (exception instanceof VariableException variableError) {
            throw new SimpleCommandExceptionType(Text.literal(variableError.getMessage())).create();
        } else {
            DataPackPlusMain.LOGGER.error("Error executing /data variable... Please report this issue!", exception);
            throw new RuntimeException(exception);
        }
    }

    @Nullable
    private static Pair<Identifier, NbtPathArgumentType.@Nullable NbtPath> getStorageNbtArgument(CommandContext<ServerCommandSource> context) {
        try {
            Identifier identifier = context.getArgument("identifier", Identifier.class);
            NbtPathArgumentType.NbtPath path = null;

            try {
                path = NbtPathArgumentType.getNbtPath(context, "path");
            } catch (IllegalArgumentException ignored) {}

            return new Pair<>(identifier, path);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private static Pair<NbtCompound, NbtCompound> getRootAndTargetCompounds(CommandContext<ServerCommandSource> context, Pair<Identifier, NbtPathArgumentType.@Nullable NbtPath> storageNbtData) throws CommandSyntaxException {
        NbtCompound root = context.getSource().getServer().getDataCommandStorage().get(storageNbtData.getLeft());
        NbtCompound nbt = root;

        if (storageNbtData.getRight() != null) {
            List<NbtElement> list = storageNbtData.getRight().get(root);

            if (list.isEmpty()) {
                throw new VariableException("Expected TAG_COMPOUND at path, got nothing.");
            }

            NbtElement element = list.getFirst();
            nbt = element.asCompound().orElseThrow(() -> new VariableException("Expected TAG_COMPOUND at path, got " + element.getNbtType().getCommandFeedbackName() + "."));
        }

        return new Pair<>(root, nbt);
    }

}