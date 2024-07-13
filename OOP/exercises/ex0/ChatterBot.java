import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * If it starts with the constant REQUEST_PREFIX, the bot returns
 * whatever is after this prefix. Otherwise, it returns one of
 * a few possible replies as supplied to it via its constructor.
 * In this case, it may also include the statement after
 * the selected reply (coin toss).
 * @author Dan Nirel
 */
class ChatterBot {
	static final String REQUEST_PREFIX = "say ";
	static  final String PLACEHOLDER_FOR_REQUESTED_PHRASE = "<phrase>";
	static final String PLACEHOLDER_FOR_ILLEGAL_REQUEST = "<request>";

	Random rand = new Random();
	String[] repliesToIllegalRequest;
	String[] legalRequestsReplies;
	String name;

	ChatterBot(String name,String[] legalRequestsReplies, String[] repliesToIllegalRequest) {
		this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
		for (int i = 0; i < repliesToIllegalRequest.length; i = i + 1) {
			this.repliesToIllegalRequest[i] = repliesToIllegalRequest[i];
		}
		this.name = name;
		this.legalRequestsReplies = new String[legalRequestsReplies.length];
		for (int i = 0; i < legalRequestsReplies.length; i = i + 1) {
			this.legalRequestsReplies[i] = legalRequestsReplies[i];
		}
	}

	String getName() {
		return this.name;
	}

	String replyTo(String statement) {
		if(statement.startsWith(REQUEST_PREFIX)) {
			//we don’t repeat the request prefix, so delete it from the reply
			return replyToLegalRequest(statement);
		}
		return replyToIllegalRequest(statement);
	}

	String replyToLegalRequest(String statement){
		statement = statement.replaceFirst(REQUEST_PREFIX, "");
		return replacePlaceholderInARandomPattern(this.legalRequestsReplies,
				PLACEHOLDER_FOR_REQUESTED_PHRASE, statement);
	}

	String replyToIllegalRequest(String statement) {
		return replacePlaceholderInARandomPattern(this.repliesToIllegalRequest,
				PLACEHOLDER_FOR_ILLEGAL_REQUEST,statement);
	}

	String replacePlaceholderInARandomPattern(String[] patterns, String placeholder, String replacement){
		int randomIndex = rand.nextInt(patterns.length);
		String pattern = patterns[randomIndex];
		return pattern.replaceAll(placeholder, replacement);
	}
}
