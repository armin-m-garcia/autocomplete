package loop.baby.autocomplete;

import java.nio.file.Path;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

//import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.vavr.collection.TreeSet;
import io.vavr.control.Either;


@RestController
public class DictionaryController {

	Logger log = LoggerFactory.getLogger(DictionaryController.class);
	// The maximum number of suggestions to return
	protected final static int SuggestionCount = 10;     
	// The property specifying the dictionary file
	protected final static String DICTIONARY_FILE_PROPERTY = "spring.dictionary.file";   
	// The default dictionary file
	protected final static String DEFAULT_DICTIONARY_FILE = "scrabble.txt";    
	
	// The dictionary domain object used to store and suggest words from.
	protected Dictionary dictionary;
	
	// The spring environment
	@Autowired
	Environment env;

	/**
	 * Resolve which dictionary file to use.
	 * 
	 * @return
	 */
	protected Either<Throwable,Path> resolveDictionaryPath() {
		// Todo:  Add error checks and report appropriate exception.
		return Either.right(Path.of(Path.of(env.getProperty(DICTIONARY_FILE_PROPERTY,DEFAULT_DICTIONARY_FILE)).toUri()));
	}
	
	/**
	 * Initialize the dictionary from the specified file.
	 * 
	 */
	@PostConstruct
	public void startUp() {		
		// Resolve the dictionary path
		dictionary = this.resolveDictionaryPath()
		// Report any issues encountered
		.peekLeft(e->log.error("Unable to resolve dictionary file. Reason="+e.getMessage()))
		// Load the dictionary
		.flatMap(p->Dictionary.from(p)
					.peekLeft(t->log.error("Unable to load dictionary file '"+p+"'."))
					.peek(d->log.info("Loaded dictionary from '"+p+"'.")))
		// Return the loaded dictionary if successful otherwise return an empty dictionary
		.getOrElse(Dictionary.builder().words(TreeSet.empty()).build())
		;
	 }
	 
	/**
	 * Suggest words based on a given prefix
	 * 
	 * @param prefix - The prefix from which to make suggestions on.
	 * @return
	 */
	@GetMapping("/autocomplete/{prefix}")
	public List<String> suggest(@PathVariable("prefix") String prefix) {
		List<String> suggestions = this.dictionary.suggest(prefix, SuggestionCount).asJava();
		
		if(suggestions.size() > 0 ) 
			// Return the suggestions
			return suggestions;		
		else
			// Throw a 418
			throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT);   
	}
}