package com.myreward.parser.test;

import java.util.Arrays;

import com.amazonaws.auth.profile.internal.AwsProfileNameLoader;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.util.StringUtils;
import com.myreward.provider.db.DynamoDBUtil;

public class DynamoDBTest {

	public static void main(String[] args) {
		DynamoDBUtil dynamoDbUtil = new DynamoDBUtil();
		dynamoDbUtil.createLocal();
		dynamoDbUtil.putItem("com.test_1.0", null);
		dynamoDbUtil.getItem("com.test_1.0");

	}

}
