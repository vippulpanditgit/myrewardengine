package com.myreward.provider.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.TableStatus;
public class DynamoDBUtil {
    private static final Logger logger = LoggerFactory.getLogger(DynamoDBUtil.class);
	
	public static String RULES_TABLE = "Tb_Rules";
	// package, version, rule, pcode
	 static  private  DynamoDB dynamoDB;
	
    /*
     * Important: Be sure to fill in your AWS access credentials in the
     * AwsCredentials.properties file before you try to run this sample and
     * configure your log4j.properties
     * 
     * http://aws.amazon.com/security-credentials
     */
	static AmazonDynamoDB dynamoDBClient;
    
	public void createLocal() {
		dynamoDBClient = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
				.build(); 
            dynamoDB = new DynamoDB(dynamoDBClient);


            try {
                System.out.println("Attempting to create table; please wait...");
                this.deleteTable(RULES_TABLE);
                createTable(RULES_TABLE, 10L, 5L, "package_version", "S");
                this.describeTable(RULES_TABLE);
 
            }
            catch (Exception e) {
                System.err.println("Unable to create table: ");
                System.err.println(e.getMessage());
            }
		
	}
    /**
     * The only information needed to create a client are security credentials
     * consisting of the AWS Access Key ID and Secret Access Key. All other
     * configuration, such as the service endpoints, are performed
     * automatically. Client parameters, such as proxies, can be specified in an
     * optional ClientConfiguration object when constructing a client.
     *
     * @see com.amazonaws.auth.BasicAWSCredentials
     * @see com.amazonaws.auth.ProfilesConfigFile
     * @see com.amazonaws.ClientConfiguration
     */
    private static void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (~/.aws/credentials).
         */
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        dynamoDBClient = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withRegion("us-west-2")
            .build();
    }
    /*
     * Get an item from the table
     */
    public static void getItem(String keyVal)
    {

        Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put("package_version", new AttributeValue(keyVal));

        GetItemRequest getItemRequest = new GetItemRequest().withTableName(RULES_TABLE).withKey(key);

        GetItemResult item = dynamoDBClient.getItem(getItemRequest);
System.out.println("Get Result: " + item);
        logger.info("Get Result: " + item);
    }
    /*
     * Describe the table
     */
    private static void describeTable(String tableName)
    {
        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
        TableDescription tableDescription = dynamoDBClient.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
        logger.info("Table Description: " + tableDescription);
    }
    /*
     * Put given item into the table
     * 
     * @param item
     */
    public void putItem(String package_version, Map<String, AttributeValue> item)
    {
        try
        {	
        	Table table = dynamoDB.getTable(RULES_TABLE);
            Item item1 = new Item().withPrimaryKey("package_version", package_version).withString("Title", "20-Bike-205")
                    .withString("Description", "205 Description").withString("BicycleType", "Hybrid")
                    .withString("Brand", "Brand-Company C").withNumber("Price", 500)
                    .withStringSet("Color", new HashSet<String>(Arrays.asList("Red", "Black")))
                    .withString("ProductCategory", "Bicycle");

            PutItemOutcome putItemResult = table.putItem(item1);
            logger.info("Result: " + putItemResult);
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
    }
    /*
     * Create new item helper
     */
    private static Map<String, AttributeValue> newItem(String name, int year, String rating, String... fans)
    {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("name", new AttributeValue(name));
        item.put("year", new AttributeValue().withN(Integer.toString(year)));
        item.put("rating", new AttributeValue(rating));
        item.put("fans", new AttributeValue().withSS(fans));

        return item;
    }

    /*
     * Waits for the table to become ACTIVE Times out after 10 minutes
     */
    private static void waitForTableToBecomeAvailable(String tableName)
    {
        logger.info("Waiting for " + tableName + " to become ACTIVE...");

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (10 * 60 * 1000);
        while (System.currentTimeMillis() < endTime)
        {
            try
            {
                Thread.sleep(1000 * 20);
            }
            catch (Exception e)
            {
            }
            try
            {
                DescribeTableRequest request = new DescribeTableRequest().withTableName(tableName);
                TableDescription tableDescription = dynamoDBClient.describeTable(request).getTable();
                String tableStatus = tableDescription.getTableStatus();
                logger.info("  - current state: " + tableStatus);
                if (tableStatus.equals(TableStatus.ACTIVE.toString()))
                    return;
            }
            catch (AmazonServiceException ase)
            {
                if (ase.getErrorCode().equalsIgnoreCase("ResourceNotFoundException") == false)
                    throw ase;
            }
        }

        throw new RuntimeException("Table " + tableName + " never went active");
    }
    private static void deleteTable(String tableName) {
        Table table = dynamoDB.getTable(tableName);
        try {
            System.out.println("Issuing DeleteTable request for " + tableName);
            table.delete();
            System.out.println("Waiting for " + tableName + " to be deleted...this may take a while...");
            table.waitForDelete();

        }
        catch (Exception e) {
            System.err.println("DeleteTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }

    private static void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
    		String partitionKeyName, String partitionKeyType) {

    	createTable(tableName, readCapacityUnits, writeCapacityUnits, partitionKeyName, partitionKeyType, null, null);
    }
    // Parameter1: table name 
    // Parameter2: reads per second 
    // Parameter3: writes per second 
    // Parameter4/5: partition key and data type
    // Parameter6/7: sort key and data type (if applicable)
    private static void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
    		String partitionKeyName, String partitionKeyType, String sortKeyName, String sortKeyType) {

    	try {

    		ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
    		keySchema.add(new KeySchemaElement().withAttributeName(partitionKeyName).withKeyType(KeyType.HASH)); // Partition
    		// key

    		ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
    		attributeDefinitions
    		.add(new AttributeDefinition().withAttributeName(partitionKeyName).withAttributeType(partitionKeyType));

    		if (sortKeyName != null) {
    			keySchema.add(new KeySchemaElement().withAttributeName(sortKeyName).withKeyType(KeyType.RANGE)); // Sort
    			// key
    			attributeDefinitions
    			.add(new AttributeDefinition().withAttributeName(sortKeyName).withAttributeType(sortKeyType));
    		}

    		CreateTableRequest request = new CreateTableRequest().withTableName(tableName).withKeySchema(keySchema)
    				.withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(readCapacityUnits)
    						.withWriteCapacityUnits(writeCapacityUnits));


    		request.setAttributeDefinitions(attributeDefinitions);

    		System.out.println("Issuing CreateTable request for " + tableName);
    		Table table = dynamoDB.createTable(request);
    		System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
    		table.waitForActive();

    	}
    	catch (Exception e) {
    		System.err.println("CreateTable request failed for " + tableName);
    		System.err.println(e.getMessage());
    	}
    }
}
