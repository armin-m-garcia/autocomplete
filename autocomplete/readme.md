1.  How does your system work? (if not addressed in comments in source)

	A dictionary is initialized by streaming the contents of the file into a TreeSet named words.  To make a suggestion, the TreeSet is iterated, an O(n) operation, searching for all words that begin with the given prefix while taking only the first 10 elements.
	
	The web service capability is facilitated by Spring Boot.   
	
2.  How will your system perform with a 10 MB file? a 100 MB file? a 1 GB file?

	The 'suggest' operation is O(n) which is OK but not optimal.  The more words, the longer the operation takes.  Moreover, the TreeSet solution is also O(n) in terms of space requirements since all words are read into RAM.  A 1GB file will require at least 1GB of RAM. The startUp operation is O(n) since every word is read into memory, taking more time.

3.  How will your system perform with 100 users? 10000 users? 1000000 users?

	The solution is CPU intensive since for every suggestion, every word in the dictionary is compared.  The more users hitting the system, the longer the response time for all.  The overall solution degrades with a greater number of users.
	    
4.  What documentation, websites, papers, etc did you consult in doing this assignment?

	https://www.vavr.io/
	https://start.spring.io/
	https://www.baeldung.com/spring-boot-command-line-arguments/
	https://stackabuse.com/how-to-return-http-status-codes-in-a-spring-boot-application/
	https://stackoverflow.com/questions/14964805/groups-of-compound-conditions-in-bash-test
	
5.  What third-party libraries or other tools does the system use? How did you choose each library or framework you used?

	I used Spring Boot, Vavr, Junit, Lombok and AssertJ largely because I am familiar with them.  For Vavr I intended to use their data structures, thinking I could leverage their Red Black Tree implementation.  I was wrong. Nevertheless, I leveraged their TreeSet for an 'OK' solution.
	
6.  What data structures did you use and why?

	I used a TreeSet because it is ordered.  The expensive operations are acceptable since they are performed at boot time.  Having said that, the search capability is very limited in performance.  A developer is relegated to doing linear searches even though their red-black tree supports the implementation. 
	
7.  How long did you spend on this exercise? 

	I spent 7 hours total.

8.  If you had unlimited more time to spend on this, how would you spend it and how would you prioritize each item?

	The suggest operation is O(n).  My goal would be to make it O(1) by leveraging a CDN to cache all calls to the web service.  The current implementation is in fact good and acceptable within the context of a CDN as long as there is elasticity when bringing up services to fulfill the startup spikes.  (Priority 1)
	
	On an algorithmic level, I would swap out TreeSet with an actual Red-Black Tree in order to perform a O(log n) traversal to find the first suggestion and then go right 'pre-order' to find the remaining. (Priority 3)
	
	As an engineering leader, I would in fact prefer the current solution with a CDN because it provides a quick turn-around time that minimizes any customer impact.  I would ensure the extra costs associated are well defended.
	
	Another improvement would be to add metrics along with the ability to monitor them.  Using these metrics, I would create bench testing. (Priority 2)
	
	The ability to shard the services based on language or a balanced distribution would be another improvement.  (Priority 4)

9.  If you were to critique your code, what would you have to say about it?

	I wished I had not gone with the Vavr TreeSet.  I wanted to use their Red-Black tree; however, in their wisdom, they made access to the data structure private.  As an engineering leader, I would pounce on anyone making that decision.
	
	Another critique is that I need to add more testing.  I'm nowhere near the threshold that I prefer; however, I also realize that I'm not going for perfection.  Delivery and quality are key.
	
	My final critique is that the code is simple in nature and robust largely to it being functional.