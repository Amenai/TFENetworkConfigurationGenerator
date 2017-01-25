package packSystem;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.Generated;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JasonUtility {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {

		JSONObject obj = new JSONObject();		
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		LocalDateTime date = LocalDateTime.now();		
		System.out.println("Date " + formatter.format(date));


		obj.put("Name", "crunchify.com"); 
		obj.put("Author", "App Shah");

		JSONArray company = new JSONArray();

		company.add("Company: eBay");
		company.add("Company: Paypal");
		company.add("Company: Google");
		obj.put("Company List", company);

		// try-with-resources statement based on post comment below :)
		try (FileWriter file = new FileWriter("C:\\Users\\Amenai\\Desktop\\save.txt")) {
			file.write(obj.toJSONString());
			System.out.println("\nJSON Object: " + obj);
		}
	}
}