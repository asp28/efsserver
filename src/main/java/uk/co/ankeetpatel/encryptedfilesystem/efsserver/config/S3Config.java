package uk.co.ankeetpatel.encryptedfilesystem.efsserver.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AWS S3 bucket config
 */
@Configuration
public class S3Config {

    @Value("${amazon.s3.accessKey}")
    private String awsId;

    @Value("${amazon.s3.secretKey}")
    private String awsKey;

    @Value("${amazon.s3.endpointUrl}")
    private String region;

    /**
     *
     * @return s3Client
     */
    @Bean
    public AmazonS3 s3client() {

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

        return s3Client;
    }

}
