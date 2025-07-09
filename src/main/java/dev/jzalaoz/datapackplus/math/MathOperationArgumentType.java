package dev.jzalaoz.datapackplus.math;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;
import oshi.util.tuples.Pair;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class MathOperationArgumentType implements ArgumentType<MathOperation> {

    private static final DynamicCommandExceptionType UNKNOWN_OPERATION_EXCEPTION = new DynamicCommandExceptionType(it ->
           Text.literal("Unknown operation '" + it + "'")
    );

    private static final Collection<Pair<String, Message>> SUGGESTIONS = MathOperation.getSuggestions();
    private static final Collection<String> EXAMPLES = Stream.of(MathOperation.ADD, MathOperation.SUBTRACT).map(it -> it.key).toList();

    @Override
    public MathOperation parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.readUnquotedString();

        try {
            return MathOperation.getFromKey(string);
        } catch (IllegalArgumentException exception) {
            throw UNKNOWN_OPERATION_EXCEPTION.create(string);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (Pair<String, Message> suggestion : SUGGESTIONS) {
            builder = builder.suggest(suggestion.getA(), suggestion.getB());
        }

        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
