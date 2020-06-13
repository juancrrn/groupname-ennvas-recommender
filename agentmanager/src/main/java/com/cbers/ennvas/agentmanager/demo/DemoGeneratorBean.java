package com.cbers.ennvas.agentmanager.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import com.cbers.ennvas.agentmanager.persistence.entity.ProductEntity;
import com.cbers.ennvas.agentmanager.persistence.repository.ProductRepository;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class DemoGeneratorBean
{

    private static final Logger log = LoggerFactory.getLogger(DemoGeneratorBean.class);

	@Autowired
    private ApplicationArguments applicationArguments;
    
    @Autowired
    private ProductRepository productRepository;

    /**
     * TODO Document method
     */
    @PostConstruct
    public void init()
    {
        log.info("Starting demo data generation.");

		/*
		 * Retrieve command line arguments (pre-validated).
		 */

		String[] args = applicationArguments.getSourceArgs();
		String demoDataJsonPath = args[0];

        /*
         * Retrieve demo data.
         */

        ProductEntity[] productArray = generate(demoDataJsonPath);
        
        /*
         * Insert demo data.
         */

        for (int i = 0; i < productArray.length; i++) {
            productRepository.save(productArray[i]);
        }

        log.info("Completed demo data generation.");
    }

    /**
     * TODO Document method
     * @param demoDataJsonPath
     * @return
     */
    public static ProductEntity[] generate(String demoDataJsonPath)
    {
        String jsonData = "";
		
		try {
            jsonData =
                new String(Files.readAllBytes(Paths.get(demoDataJsonPath)), StandardCharsets.UTF_8);
		} catch (IOException e) {
            e.printStackTrace();
        }
        
        log.info("JSON demo data retrieved successfully.");
		
		Gson gson = new Gson();
		
        return gson.fromJson(jsonData, ProductEntity[].class);
    }
}