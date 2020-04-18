package com.cbers.ennvas.recommender.core;

/**
 * Represents a stored (persistent) value of a product.
 * 
 * @author Juan Francisco Carrión Molina
 * @author Raquel Pérez González de Ossuna
 * @author Olga Posada Iglesias
 * @author Nicolás Pardina Popp
 * @author Melany Daniela Chicaiza Quezada
 * 
 * @version 0.1
 */

public class StoredValue
{
	/**
	 * @var Product name
	 */
	private String name;
	
	/**
	 * @var Product type
	 */
	private String type;
	
	/**
	 * @var Product brand
	 */
	private String brand;
	
	/**
	 * @var Product price
	 */
	private double price;
	
	/**
	 * @var Product shipping price
	 */
	private double shippingPrice;
	
	/**
	 * @var Product shipping time
	 * 
	 * Measured in days.
	 */
	private int shippingTime;
	
	/**
	 * @var Product stock
	 *  
	 * Measured in product units.
	 */
	private int stock;
	
	/**
	 * @var Product description
	 */
	private String description;
	
	/**
	 * @var Product rating.
	 * 
	 * In range 0.0 to 5.0.
	 */
	private double rating;

	/**
	 * @var Unicode characters to be replaced.
	 */
	private static final String UNICODE =
		"ÀàÈèÌìÒòÙùÁáÉéÍíÓóÚúÝýÂâÊêÎîÔôÛûŶŷÃãÕõÑñÄäËëÏïÖöÜüŸÿÅåÇçŐőŰű";
	
	/**
	 * @var ASCII characters to replace with.
	 */
	private static final String PLAIN_ASCII =
		"AaEeIiOoUuAaEeIiOoUuYyAaEeIiOoUuYyAaOoNnAaEeIiOoUuYyAaCcOoUu";

	/**
	 * @var Product utility
	 * 
	 * Calculated statically and on demand.
	 */
	protected double utility;

	/**
	 * Constructs a StoredValue object.
	 * 
	 * @param name
	 * @param type
	 * @param brand
	 * @param price
	 * @param shippingPrice
	 * @param shippingTime
	 * @param stock
	 * @param description
	 * @param rating
	 */
	public StoredValue(
		String name,
		String type,
		String brand,
		double price,
		double shippingPrice,
		int shippingTime,
		int stock,
		String description,
		double rating
	)
	{
		this.name = name;
		this.type = type;
		this.brand = brand;
		this.price = price;
		this.shippingPrice = shippingPrice;
		this.shippingTime = shippingTime;
		this.stock = stock;
		this.description = description;
		this.rating = rating;
	}
	
	/**
	 * Converts an Unicode string to plain ASCII.
	 * 
	 * @param str String to convert
	 * 
	 * @return Converted string
	 */
	public static String strToAscii(String str) {
	    if (str == null) {
	        return null;
	    }
	    
	    StringBuilder sb = new StringBuilder();
	    
	    for (int i = 0; i < str.length(); i++) {
	        char c = str.charAt(i);
	        int pos = UNICODE.indexOf(c);
	        if (pos > -1) {
	            sb.append(PLAIN_ASCII.charAt(pos));
	        } else {
	            sb.append(c);
	        }
	    }
	    
	    return sb.toString();
	}

	/**
	 * Utility function
	 * 
	 * Returns an utility value based on a query.
	 * 
	 * @param query Query base
	 * 
	 * @return Utility value
	 */
	public double utilityFunction(Query query)
	{

		double utility = 0;
		
		/*
		 * Every "if" statement checks if the param has a default value.
		 * If value is default, param is not checked, it is taken as null
		 * (not specified).
		 * 
		 * Negative utility values are allowed, as getting any result is a
		 * priority.
		 */
		
		/* Query minimum price. */
		if (query.getPriceMin() > 0.0 && query.getPriceMin() > this.getPrice()) {
			utility--;
		}

		/* Query maximum price. */
		if (query.getPriceMax() > 0.0 && query.getPriceMax() < this.getPrice()) {
			utility--;
		}

		/* Query free shipping. */
		if (query.isShippingFree() && ! this.isShippingFree()) {
			utility--;
		}

		/* Query maximum shipping time. */
		if (query.getMaxShippingTime() > 0 && query.getMaxShippingTime() < this.getShippingTime()) {
			utility--;
		}

		/* Query available. */
		if (query.getAvailable() && ! this.isAvailable()) {
			utility--;
		}

		/* Query minimum rating. */
		if (query.getMinRating() > 0 && query.getMinRating() > this.getRating()) {
			utility--;
		}
		
		/*
		 * Ignore special symbols: allow only a-z, A-Z, 0-9 and space.
		 * Also compress spaces: "    " and " " are replaced with ";".
		 */
		
		String phrase = query.getPhrase().replaceAll("[^\\sA-Za-z0-9]", "");
		phrase = phrase.replaceAll("[\\s]+", ";");
		
		/*
		 * Semicolon-split the phrase.
		 */
		
		String[] phraseArray = phrase.split(";");

		/*
		 * TODO: Qué pasa con las palabras de una sola letra.
		 * TODO: Qué pasa con las palabras sin significado (preposiciones, etc.).
		 * TODO: Qué pasa con las palabras repetidas.
		 */

		for (String p : phraseArray) {
			/*
			 * All strings are ASCII-compared to avoid diacritic mistakes.
			 * 
			 * Lowercase the phrase.
			 */
			
			String lcp = StoredValue.strToAscii(p).toLowerCase();
			
			/*
			 * Check every attribute that could be specified in the phrase.
			 */
			
			if (StoredValue.strToAscii(this.name).toLowerCase().contains(lcp)) {
				utility++;
			}

			if (StoredValue.strToAscii(this.type).toLowerCase().contains(lcp)) {
				utility++;
			}

			if (StoredValue.strToAscii(this.brand).toLowerCase().contains(lcp)) {
				utility++;
			}

			if (StoredValue.strToAscii(this.description).toLowerCase().contains(lcp)) {
				utility++;
			}
		}

		return utility;
	}

	public String getName()
	{
		return this.name;
	}

	public String getType()
	{
		return this.type;
	}

	public String getBrand()
	{
		return this.brand;
	}

	public double getPrice()
	{
		return this.price;
	}

	public double getShippingPrice()
	{
		return this.shippingPrice;
	}

	public boolean isShippingFree()
	{
		return this.shippingPrice == 0;
	}

	public int getShippingTime()
	{
		return this.shippingTime;
	}

	public int getStock()
	{
		return this.stock;
	}
	
	public boolean isAvailable()
	{
		return this.stock > 0;
	}

	public String getDescription()
	{
		return this.description;
	}

	public double getRating()
	{
		return this.rating;
	}

	/**
	 * Pre-calculates and stores the product utility.
	 * 
	 * @param query Query to use in the utility function
	 */
	public void calculateUtility(Query query)
	{
		this.utility = this.utilityFunction(query);
	}

	public double getUtility()
	{
		return this.utility;
	}
	
	@Override
	public String toString() {
		String s =
			"StoredValue: {\n" +
			"    name: \"" + this.getName() + "\",\n" +
			"    type: \"" + this.getType() + "\",\n" +
			"    brand: \"" + this.getBrand() + "\",\n" +
			"    price: " + this.getPrice() + ",\n" +
			"    shippingPrice: " + this.getShippingPrice() + ",\n" +
			"    shippingTime: " + this.getShippingTime() + ",\n" +
			"    stock: " + this.getStock() + ",\n" +
			"    description: \"" + this.getDescription() + "\",\n" +
			"    rating: " + this.getRating() + "\n" +
			"}";
		;
		
		return s;
	}

}