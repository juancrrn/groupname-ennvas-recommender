package com.cbers.ennvas.recommender.domain.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a persistent value of a Product.
 * 
 * @author Juan Francisco Carrión Molina
 * @author Raquel Pérez González de Ossuna
 * @author Olga Posada Iglesias
 * @author Nicolás Pardina Popp
 * @author Melany Daniela Chicaiza Quezada
 * 
 * @version 0.0.2
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "type",
    "brand",
    "price",
    "stock",
    "description",
    "rating",
    "shipping_price",
    "shipping_time"
})
public class Product
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
	 * @var Product utility
	 * 
	 * Calculated statically and on demand.
	 */
	@JsonIgnore
	private double utility;

	public Product(Product p)
	{
		this.brand = p.brand;
		this.description = p.description;
		this.name = p.name;
		this.price = p.price;
		this.rating = p.rating;
		this.shippingPrice = p.shippingPrice;
		this.shippingTime = p.shippingTime;
		this.stock = p.stock;
		this.type = p.type;
		this.utility = p.utility;
	}

	@JsonIgnore
	public boolean isShippingFree() {
		return this.shippingPrice == 0;
	}

	@JsonIgnore
	public boolean isAvailable() {
		return this.stock > 0;
	}
	
	@Override
	public String toString()
	{
		return "[\n" +
		"  name: " + this.getName() + ",\n" +
		"  type: " + this.getType() + ",\n" +
		"  brand: " + this.getBrand() + ",\n" +
		"  price: " + this.getPrice() + ",\n" +
		"  stock: " + this.getStock() + ",\n" +
		"  description: " + this.getDescription() + ",\n" +
		"  rating: " + this.getRating() + ",\n" +
		"  shippingPrice: " + this.getShippingPrice() + ",\n" +
		"  shippingTime: " + this.getShippingTime() + ",\n" +
		"]";
	}
} 