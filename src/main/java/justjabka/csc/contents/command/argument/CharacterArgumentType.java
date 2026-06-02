package justjabka.csc.contents.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.registries.CSCCharacters;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.Identifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.resources.Identifier.ERROR_INVALID;

public class CharacterArgumentType implements ArgumentType<Identifier> {
    private static final Collection<String> EXAMPLES = Arrays.asList("swordsman", "csc:swordsman");
    private static final Set<Identifier> VALUES = CSCCharacters.getCharacters().keySet();

    public static CharacterArgumentType character() {
        return new CharacterArgumentType();
    }

    @Override
    public Identifier parse(StringReader reader) throws CommandSyntaxException {
        BaseCharacter character = CSCCharacters.getByKey(Identifier.read(reader));

        if (character == null) {
            throw ERROR_INVALID.createWithContext(reader);
        } else {
            return character.getKey();
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return context.getSource() instanceof SharedSuggestionProvider
                ? SharedSuggestionProvider.suggest(VALUES.stream().map(Identifier::toString), builder)
                : Suggestions.empty();
    }

    public static Identifier getCharacter(final CommandContext<CommandSourceStack> context, final String argumentName) {
        return context.getArgument(argumentName, Identifier.class);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
