package loop.baby.autocomplete;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

import io.vavr.collection.List;
import io.vavr.collection.TreeSet;
import io.vavr.control.Either;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.experimental.Accessors;

/**
 * A Dictionary stores a set of words initialized from a file where each word is stored on its own line.
 * 
 * @author armin
 *
 */
@With
@Accessors(fluent=true)
@Builder(toBuilder = true)
@Value
public class Dictionary {
	
	protected TreeSet<String> words;          // The data structured used to store and query suggestions.
	
	/**
	 * Create a dictionary from the given file.
	 * 
	 * @param file  - The file where the words are stored at.
	 * @return
	 */
	public static Either<Throwable,Dictionary> from(Path file) {
		return Dictionary.from(file, s->true);
	}
	
	/**
	 * Create a dictionary from the given file and filter.
	 * 
	 * @param file   - The file containing the words.
	 * @param filter - A filter used to limit the words stored in the dictionary.
	 * @return
	 */
	public static Either<Throwable,Dictionary> from(Path file, Predicate<String> filter) {
		// Use a try-resource to read all the files into the data structure
		try ( java.util.stream.Stream<String> stream = Files.lines(file)) {
			return Either.right(Dictionary.builder().words(TreeSet.ofAll(stream.filter(filter))).build());
		} catch (IOException e) {
			return Either.left(e);
		} 
		
		//  Vavr does a poor job of emulating try...ugh!
//		return Try.withResources(() -> Files.lines(file))
//		.of(stream->Stream.ofAll(stream)
//						  .filter(filter)							  
//				)
//		.map(s->TreeSet.ofAll(s.toJavaStream()))
//		.map(tm->Dictionary.builder().words(tm).build())
//		.toEither()
//		;
	}
			
	/**
	 * Based on the given <prefix>, make up to <amount> suggested words.
	 * 
	 * @param prefix - The prefix to from which a suggestion is made.
	 * @param amount - The maximum number of suggestions to make.
	 * @return
	 */
	public List<String> suggest(final String prefix, int amount) {		
		return this.words()                   // Get the list of words
		.filter(w->w.startsWith(prefix))      // Filter on all words that start with the given prefix
		.take(amount)                         // Take up to <amount> words
		.collect(List.collector())            // Return into a list
		;
	}
	
	/**
	 * Return the number of words in the dictionary.
	 * 
	 * @return
	 */
	public int size() {
		return this.words().size();
	}
}
