package com.instana.demo.sdk.ifcid;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.instana.sdk.annotation.Span;
import com.instana.sdk.support.SpanSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

@SpringBootApplication
@SuppressWarnings("unused")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Configuration
    public static class RestTemplateConfiguration {

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

	}

	@RestController
	@RequestMapping(path = "/api/hello")
	public class RestEndpoint {

	    private final Random random = new Random();

		@Autowired
		private RestTemplate restTemplate;

		@GetMapping
		@ResponseStatus(HttpStatus.OK)
        @Span(value = "sayHello", type = Span.Type.INTERMEDIATE /* default */)
		public JsonNode sayHello() {
			final String traceId = SpanSupport.traceId(Span.Type.INTERMEDIATE, "sayHello");
			final String parentId = SpanSupport.spanId(Span.Type.INTERMEDIATE, "sayHello");

			/*

        "IFCIDs": [{
            "ACE": 357810368
        }, {
            "IFCID 66 - Close Cursor": [{
                "Data": ["Location Name:LOCDS0I", "Collection ID:NULLID", "Program Name:SYSLH100", "Consistency Token:5359534C564C3031", "Type of Statement:CLOSE", "Cursor Name Length:18", "Cursor Name:SQL_CURLH100C1", "Close Statement Type:IMPLICIT", "Statement Number:1", "Query Command ID:0000020D00970068", "Query Instance ID:0000000000000000"]
            }]
        }, {
            "IFCID 58 - End SQL": [{
                "Statement Section": [{
                    "SQL Communication Area": ["SQLCAID:SQLCA", "Length of the SQLCA:136", "SQLCODE:100     Successful execution, but with a warning condition or other information.", "SQLERRML:0     (Length indicator for SQLERRMC)", "SQLERRMC:", "SQLERRP:DSNXRFN     (Provides a product signature and, in case of an error, diagnostic information such as the name of the module that detected the error. In all cases, the first three characters are 'DSN' for DB2 for z/OS)", "SQLERRD(1):49947186    (Contains the number of Rows in a result table, when the cursor position is after the last row. On successful return from an SQL procedure, contains the return status value from the SQL procedure.)", "SQLERRD(2):0    (Contains the number of Rows in a result table, when the cursor position is after the last row.)", "SQLERRD(3):0    Statement fails, indicating that all changes made in executing the statement canceled.", "SQLERRD(4):4.2949673E9    (Generally, contains timerons, a short floating-point value that indicates a rough relative estimate of resources required)", "SQLERRD(5):0    (Contains the position or column of a syntax error for a PREPARE or EXECUTE IMMEDIATE statement)", "SQLERRD(6):0   (Contains an internal error code)", "SQLWARN0: no other indicator is set to a warning condition. So we can ignore them.", "SQLWARN4: Cursor is not scrollable.", "SQLSTATE:02000 (Contains a return code for the outcome of the most recent execution of an SQL statement)"]
                }, {
                    "Statement Section": ["Location Name:LOCDS0I", "Collection ID:NULLID", "Program Name:SYSLH100", "Consistency Token:5359934C564C3031", "RDI number:0", "Statement Number:1", "Query Command ID:0000020DB0C68", "Query Instance ID:0000000000000000", "Type of Statement:OPEN", "Expansion reason:NO_EXPANSION"]
                }]
            }, {
                "ID Section": [{
                    "ID Section: 1": ["Rows processed:48", "Rows looked at:298", "Rows qualified:8", "Rows rdf qualified:4", "Rows inserted:0", "Rows updated:0", "Rows deleted:0", "Pages scanned:60", "Additional pages scanned:0", "Additional rows deleted:0", "Rows scanned in lob:0", "Count of lob:0"]
                }, {
                    "ID Section: 2": ["Rows processed:1039", "Rows looked at:1039", "Rows qualified:0", "Rows rdf qualified:0", "Rows inserted:0", "Rows updated:0", "Rows deleted:0", "Pages scanned:163", "Additional pages scanned:0", "Additional rows deleted:0", "Rows scanned in lob:0", "Count of lob:0"]
                }]
            }, {
                "TimeValue Section": ["Statement Type:DYNAMIC", "Statement ID:1021259", "Number of synch buffer reads:37", "Number get pages:223", "Number rows executed:1039", "Number rows processed:2", "Sorts:0", "Idex scans:7", "Table space scans:0", "Buffer writes:0", "Parallel groups:0", "DB2 elapsed time:12905", "DB2 cpu time:941", "Synchronous io:9371.0", "lock:0.0", "Synchronous execution:0.0", "Globallock:0.0", "Read activity:2457.0", "Write activity:0.0", "Exceeded limits:0", "Not enough storage:0", "Latch:0.0", "Page latch:0.0", "Drain lock:0.0", "Clain count:0.0", "Logwriter:0.0"]
            }]
        }]

			 */
			final JsonNode ifcid66 = JsonNodeFactory.instance.objectNode()
					.set("Data", JsonNodeFactory.instance.objectNode()
							.put("Location Name", "LOCDS0I")
							.put("Collection ID", "NULLID")
							.put("Program Name", "SYSLH100")
							.put("Consistency Token", "5359534C564C3031")
							.put("Type of Statement", "CLOSE")
							.put("Cursor Name Length", 18)
							.put("Cursor Name", "SQL_CURLH100C1")
							.put("Close Statement Type", "IMPLICIT")
							.put("Statement Number", 1)
							.put("Query Command ID", "0000020D00970068")
							.put("Query Instance ID", "0000000000000000")
					);

			// Simplified
			final Map<String, JsonNode> sectionNodes = new LinkedHashMap<>();
			sectionNodes.put("Statement Section", JsonNodeFactory.instance.objectNode()
					.put("SQLCAID", "SQLCA")
			);
			sectionNodes.put("ID Section", JsonNodeFactory.instance.objectNode()
					.put("Rows processed", 48)
					.put("Rows looked at", 298)
			);
			sectionNodes.put("TimeValue Section", JsonNodeFactory.instance.objectNode()
                    .put("Statement Type", "DYNAMIC")
					.put("Statement ID", "1021259")
			);

			final JsonNode ifcid58 = JsonNodeFactory.instance.objectNode()
					.setAll(sectionNodes);

			final Map<String, JsonNode> ifcidNodes = new LinkedHashMap<>();
			ifcidNodes.put("IFCID 66 - Close Cursor", ifcid66);
			ifcidNodes.put("IFCID 58 - End SQL", ifcid58);

			final JsonNode ifcidJson = JsonNodeFactory.instance.objectNode()
					.put("ACE", 357810368)
					.setAll(ifcidNodes);

			final JsonNode dbNode = JsonNodeFactory.instance.objectNode()
                    .put("instance", "zOS")
                    .put("query", "some_query");

			final Map<String, JsonNode> dataMap = new LinkedHashMap<>();
                    dataMap.put("ifcid", ifcidJson);
                    dataMap.put("tags", JsonNodeFactory.instance.objectNode().set("db", dbNode));

            final String entrySpanId =  UUID.randomUUID().toString();

			final JsonNode exitSpanJson = JsonNodeFactory.instance.objectNode()
					.put("spanId", entrySpanId)
					.put("parentId", parentId)
					.put("traceId", traceId)
					.put("timestamp", System.currentTimeMillis())
					.put("duration", 500)
					.put("name", "ifcid-query-exit")
					.put("type", Span.Type.EXIT.name())
					.put("error", false)
					.set("data", JsonNodeFactory.instance.objectNode()
							.setAll(dataMap));

			final JsonNode entrySpanJson = JsonNodeFactory.instance.objectNode()
					.put("spanId", UUID.randomUUID().toString())
					.put("parentId", entrySpanId)
					.put("traceId", traceId)
					.put("timestamp", System.currentTimeMillis() + 600)
					.put("duration", random.nextInt(10))
					.put("name", "ifcid-query-entry")
					.put("type", Span.Type.ENTRY.name())
					.put("error", false)
					.set("data", JsonNodeFactory.instance.objectNode()
							.setAll(dataMap));

			for (JsonNode spanNode : Arrays.asList(entrySpanJson, exitSpanJson)) {
				final ResponseEntity<JsonNode> response = restTemplate
						.postForEntity(URI.create("http://localhost:42699/com.instana.plugin.generic.trace"),
								spanNode, JsonNode.class);

				if (!response.getStatusCode().is2xxSuccessful()) {
					throw new IllegalStateException(String.format("Could not submit trace data to the Instana agent: %s", response.getStatusCode()));
				}
			}

			return JsonNodeFactory.instance.arrayNode().add(entrySpanJson).add(exitSpanJson);
		}

	}

}