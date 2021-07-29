package loop.baby.autocomplete;

import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import io.vavr.collection.List;

class DictionaryTests {
	
	
	@Test
	void test000() throws Throwable {
		URI r = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().resolve("dictionary.txt");
//		URI r = Dictionary.class.getProtectionDomain().getCodeSource().getLocation().toURI().resolve("scrabble.txt");
		
		assertThat(Dictionary.from(Paths.get(r)))
		.hasRightValueSatisfying(d->assertEquals(10,d.size()))
		.hasRightValueSatisfying(d->assertThat(d.suggest("ACT", 10)).hasSameElementsAs(List.of("ACTINOID","ACTINOIDS","ACTINOLITE")))
		;	
		
		assertThat(Dictionary.from(Paths.get(r)))
		.hasRightValueSatisfying(d->assertEquals(10,d.size()))
		.hasRightValueSatisfying(d->assertThat(d.suggest("A", 10)).hasSameElementsAs(List.of("AA","AAH","ACTINOID","ACTINOIDS","ACTINOLITE")))
		;
		
		assertThat(Dictionary.from(Paths.get(r)))
		.hasRightValueSatisfying(d->assertEquals(10,d.size()))
		.hasRightValueSatisfying(d->assertThat(d.suggest("YONIC", 10)).hasSameElementsAs(List.of("YONIC")))
		;	

		assertThat(Dictionary.from(Paths.get(r)))
		.hasRightValueSatisfying(d->assertEquals(10,d.size()))
		.hasRightValueSatisfying(d->assertThat(d.suggest("ZZZ", 10)).hasSameElementsAs(List.empty()))
		;	
	}
}
