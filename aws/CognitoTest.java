public class CognitoTest {

    String clientId = "my-client-id";
    String userPoolId = "my-user-pool-id";
    String region = "ap-northeast-1";

    public AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("my-access-key", "my-secret-key");
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(region)
                .build();
    }

    class AuthenticationResultModel {

        String accessToken = "";
        String idToken = "";
        String refreshToken = "";
        String tokenType = "";

        public AuthenticationResultModel(String accessToken, String idToken, String refreshToken, String tokenType) {
            this.accessToken = accessToken;
            this.idToken = idToken;
            this.refreshToken = refreshToken;
            this.tokenType = tokenType;
        }
    }

    public AuthenticationResultModel signIn() {
        AuthenticationResultModel model = null;
        String username = "client1-admin";
        String password = "12345678";
        String newpassword = "87654321";
        try {
            AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();
            final Map<String, String> authParams = new HashMap<>();
            authParams.put("USERNAME", username);
            authParams.put("PASSWORD", password);

            final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();
            authRequest.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                    .withClientId(clientId)
                    .withUserPoolId(userPoolId)
                    .withAuthParameters(authParams);

            AdminInitiateAuthResult result = cognitoClient.adminInitiateAuth(authRequest);

            System.out.println("list challengeResult : " + result.getChallengeName());
            Map<String, String> challengeResult = result.getChallengeParameters();
            for (Map.Entry<String, String> entry : challengeResult.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println(key + " : " + value);
            }

            AuthenticationResultType authenticationResult = null;
            //Has a Challenge
            if (!result.getChallengeName().trim().isEmpty()) {
                //If the challenge is required new Password validates if it has the new password variable.
                if (ChallengeNameType.NEW_PASSWORD_REQUIRED.toString().equals(result.getChallengeName())) {
                    //we still need the username
                    final Map<String, String> challengeRequest = new HashMap<>();
                    challengeRequest.put("USERNAME", username);
                    challengeRequest.put("PASSWORD", password);
                    challengeRequest.put("userAttributes.name", "faber member");
                    challengeRequest.put("userAttributes.gender", "male");
                    challengeRequest.put("userAttributes.custom:account_type", "faber-admin");
                    challengeRequest.put("userAttributes.email", "lamduthach@gmail.com");
                    //add the new password to the params map
                    challengeRequest.put("NEW_PASSWORD", newpassword);
                    //populate the challenge response
                    final AdminRespondToAuthChallengeRequest request = new AdminRespondToAuthChallengeRequest();
                    request.withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                            .withChallengeResponses(challengeRequest)
                            .withClientId(clientId)
                            .withUserPoolId(userPoolId)
                            .withSession(result.getSession());
                    AdminRespondToAuthChallengeResult resultChallenge = cognitoClient.adminRespondToAuthChallenge(request);
                    authenticationResult = resultChallenge.getAuthenticationResult();
                } else {
                    //has another challenge
                    System.out.println("another challenge : " + result.getChallengeName());
                }
            } else {
                //Doesn't have a challenge
                authenticationResult = result.getAuthenticationResult();
            }

            model = new AuthenticationResultModel(
                    authenticationResult.getAccessToken(),
                    authenticationResult.getIdToken(),
                    authenticationResult.getRefreshToken(),
                    authenticationResult.getTokenType());

            System.out.println("AccessToken : " + authenticationResult.getAccessToken());
            System.out.println("IdToken : " + authenticationResult.getIdToken());
            System.out.println("RefreshToken : " + authenticationResult.getRefreshToken());
            System.out.println("TokenType : " + authenticationResult.getTokenType());

            cognitoClient.shutdown();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return model;
    }

    public void getUser() {
        String username = "faber-admin";
        AdminGetUserRequest adminGetUserRequest = new AdminGetUserRequest()
                .withUserPoolId(userPoolId)
                .withUsername(username);
        AdminGetUserResult adminGetUserResult = getAmazonCognitoIdentityClient().adminGetUser(adminGetUserRequest);
        List<AttributeType> attributeTypeList =adminGetUserResult.getUserAttributes();
        System.out.println("Attributes :");
        for (AttributeType attributeType : attributeTypeList) {
            System.out.println(attributeType.getName() + " : " + attributeType.getValue());
        }
    }
}
