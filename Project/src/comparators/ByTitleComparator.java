package comparators;


import java.util.Comparator;

import main.Song;

public class ByTitleComparator implements Comparator<Song> {

	@Override
	public int compare(Song user1, Song user2) {
		
		if (user1.getTitle().equals(user2.getTitle())){          // if titles are the same
			
			if (user1.getArtist().equals(user2.getArtist())) {      // if artists are the same
				
				return user1.getTrackId().compareTo(user2.getTrackId());      // return comparison of trackIds
			}
			
			else {
				
				return user1.getArtist().compareTo(user2.getArtist());         // if titles aren't the same, return the comparison of titles
			}
		} else {
			
			return user1.getTitle().compareTo(user2.getTitle());			// if artists aren't the same, return the comparison of artists
		}
	}

}

