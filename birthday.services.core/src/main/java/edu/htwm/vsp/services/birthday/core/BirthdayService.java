package edu.htwm.vsp.services.birthday.core;

import java.util.List;

public interface BirthdayService {

	/**
	 * Get some hints for buying a birthday present.
	 * 
	 * @param name
	 *            The name of the user to get suggestions for.
	 * 
	 * @return Some suggestions if found, null else.
	 */
	BirthDayInfo getSuggestionsFor(String name);

	/**
	 * Checks whether suggestions for the given user exist.
	 * 
	 * @param name
	 *            The name of the user to get suggestions for.
	 * @return True if suggestions were found for a user with the given name,
	 *         false otherwise.
	 */
	boolean hasSuggestionsFor(String name);

	/**
	 * Add some suggestions.
	 * 
	 * @param name
	 *            The name of the user the suggestions belongs to.
	 * @param dayOfMonth
	 * @param month
	 * @param hobbies
	 * 
	 * @return The new {@link BirthDayInfo}
	 */
	BirthDayInfo addSuggestion(String name, int dayOfMonth, int month, List<String> hobbies);

}
