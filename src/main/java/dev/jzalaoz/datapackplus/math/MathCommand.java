package dev.jzalaoz.datapackplus.math;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
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
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class MathCommand {

    public static final Collection<Pair<String, Message>> OPERATION_SUGGESTIONS = MathOperation.getSuggestions();
    public static final SuggestionProvider<ServerCommandSource> OPERATION_SUGGESTION_PROVIDER = (context, builder) -> {
        String input = builder.getRemaining().toLowerCase(Locale.ROOT);

        for (Pair<String, Message> suggestion : OPERATION_SUGGESTIONS) {
            if (suggestion.getLeft().toLowerCase().startsWith(input)) {
                builder.suggest(suggestion.getLeft(), suggestion.getRight());
            }
        }

        return builder.buildFuture();
    };

    public static void register(LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder) {
        literalArgumentBuilder.then(CommandManager.literal("math")
                .then(CommandManager.argument("operation", StringArgumentType.word())
                        .suggests(OPERATION_SUGGESTION_PROVIDER)
                        .then(CommandManager.literal("storage")
                                .then(CommandManager.argument("identifier", IdentifierArgumentType.identifier())
                                        .suggests(DataPackPlusMain.STORAGE_SUGGESTION_PROVIDER)
                                        .executes(context ->
                                                executeStorage(context,
                                                        MathOperation.getFromKey(StringArgumentType.getString(context, "operation")),
                                                        IdentifierArgumentType.getIdentifier(context, "identifier"),
                                                        null
                                                )
                                        )
                                        .then(CommandManager.argument("path", NbtPathArgumentType.nbtPath())
                                                .executes(context ->
                                                        executeStorage(context,
                                                                MathOperation.getFromKey(StringArgumentType.getString(context, "operation")),
                                                                IdentifierArgumentType.getIdentifier(context, "identifier"),
                                                                NbtPathArgumentType.getNbtPath(context, "path")
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("with")
                                .then(CommandManager.argument("nbt", NbtCompoundArgumentType.nbtCompound())
                                        .executes(context ->
                                                executeWith(context,
                                                        MathOperation.getFromKey(StringArgumentType.getString(context, "operation")),
                                                        NbtCompoundArgumentType.getNbtCompound(context, "nbt")
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static int executeStorage(
            CommandContext<ServerCommandSource> context,
            MathOperation operation,
            Identifier storageIdentifier,
            @Nullable NbtPathArgumentType.NbtPath path
    ) throws CommandSyntaxException {
        try {
            NbtCompound root = context.getSource().getServer().getDataCommandStorage().get(storageIdentifier);
            NbtCompound nbt = root;

            if (path != null) {
                List<NbtElement> list = path.get(root);

                if (list.isEmpty()) {
                    throw new MathOperationException("Expected TAG_COMPOUND at path, got nothing.");
                }

                NbtElement element = list.getFirst();
                nbt = element.asCompound().orElseThrow(() -> new MathOperationException("Expected TAG_COMPOUND at path, got " + element.getNbtType().getCommandFeedbackName() + "."));
            }

            NbtList output = processOperation(context, operation, nbt);
            nbt.put("out", output);
            context.getSource().getServer().getDataCommandStorage().set(storageIdentifier, root);
            context.getSource().sendMessage(Text.literal("Successfully performed operation."));
        } catch (Exception exception) {
            handleException(exception);
        }
        return 0;
    }

    private static int executeWith(
            CommandContext<ServerCommandSource> context,
            MathOperation operation,
            NbtCompound nbt
    ) throws CommandSyntaxException {
        try {
            long startTime = System.nanoTime();
            NbtList output = processOperation(context, operation, nbt);
            long microSecondsTook = (System.nanoTime() - startTime) / 1000;

            MutableText result = Text.literal("[Math Report]:").withColor(Colors.YELLOW);
            result.append(Text.literal("\nTook " + microSecondsTook + "µs").withColor(Colors.LIGHT_GRAY));
            result.append("\nOperation: ").append(Text.literal(operation.key + " (" + operation.ordinal() + ")").withColor(Colors.LIGHT_GRAY));
            result.append("\nInput: ").append(Text.literal(nbt.getListOrEmpty("in").toString()).withColor(Colors.LIGHT_GRAY));
            result.append("\nOutput: ").append(Text.literal(output.toString()).withColor(Colors.LIGHT_GRAY));
            context.getSource().sendMessage(result);
        } catch (Exception exception) {
            handleException(exception);
        }
        return 0;
    }

    private static NbtList processOperation(CommandContext<ServerCommandSource> context, MathOperation operation, NbtCompound nbt) throws CommandSyntaxException {
        NbtList input = nbt.getListOrEmpty("in");
        NbtList output = new NbtList();
        operation.handler.evaluate(input, output, context.getSource());
        return output;
    }

    private static void handleException(Exception exception) throws CommandSyntaxException {
        if (exception instanceof MathOperationException mathError) {
            throw new SimpleCommandExceptionType(Text.literal(mathError.getMessage())).create();
        } else {
            DataPackPlusMain.LOGGER.error("Error executing /data math... Please report this issue!", exception);
            throw new RuntimeException(exception);
        }
    }

}
