package com.myreward.provider.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
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
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.TableStatus;
public class DynamoDB {
    private static final Logger logger = LoggerFactory.getLogger(DynamoDB.class);
	
	public static String TABLENAME = "Tb_Rules";
	// package, version, rule, pcode
	
    /*
     * Important: Be sure to fill in your AWS access credentials in the
     * AwsCredentials.properties file before you try to run this sample and
     * configure your log4j.properties
     * 
     * http://aws.amazon.com/security-credentials
     */
	static AmazonDynamoDB dynamoDBClient;
    
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
    private static void getItem(String keyVal)
    {

        Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put("name", new AttributeValue(keyVal));

        GetItemRequest getItemRequest = new GetItemRequest().withTableName(TABLENAME).withKey(key);

        GetItemResult item = dynamoDBClient.getItem(getItemRequest);

        logger.info("Get Result: " + item);
    }
    /*
     * Describe the table
     */
    private static void describeTable()
    {
        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(TABLENAME);
        TableDescription tableDescription = dynamoDBClient.describeTable(describeTableRequest).getTable();
        logger.info("Table Description: " + tableDescription);
    }
    /*
     * Put given item into the table
     * 
     * @param item
     */
    private static void putItem(Map<String, AttributeValue> item)
    {

        try
        {

            PutItemRequest putItemRequest = new PutItemRequest(TABLENAME, item);
            PutItemResult putItemResult = dynamoDBClient.putItem(putItemRequest);
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
}
