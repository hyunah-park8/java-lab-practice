
package api.assignment;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Collections;

public class ContactManager {
	private HashMap<String, String> contacts;
	
	public ContactManager() {
		contacts = new HashMap<>();
	}
	
	public boolean add(String name, String phone) {
		
		if (contacts.containsKey(name)) {
			return false;
		}
		else {
			contacts.put(name, phone);
		}
		return true;
	}
	
	public String find(String name) {
		return contacts.get(name);	
	}
	
	public boolean delete(String name) {
		if (contacts.containsKey(name)) {
			contacts.remove(name);
			return true;
		}
		return false;
	}
	
	public ArrayList<String> show() {
		ArrayList<String> resultList = new ArrayList<>();
		
		ArrayList<String> names = new ArrayList<>(contacts.keySet());
		
		Collections.sort(names);
		
		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);
			String phone = contacts.get(name);
			
			resultList.add(name +" " + phone);
		}
		return resultList;
	}

}
