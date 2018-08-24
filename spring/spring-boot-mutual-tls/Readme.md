## Testing with self signed certificates:

 1. **Generating the Server Keystore:**
`keytool -genkeypair -alias secure-server -keyalg RSA -dname "CN=localhost,OU=myorg,O=myorg,L=mycity,S=mystate,C=no" -keypass secret -keystore server-keystore.jks -storepass secret`

 2. **Generating the Client1 Keystore:** 
 `keytool -genkeypair -alias secure-client1 -keyalg RSA -dname "CN=secure-client1,OU=myorg,O=myorg,L=mycity,S=mystate,C=no" -keypass secret -keystore client1-keystore.jks -storepass secret`

 3. **Generating the Client2 Keystore:**
`keytool -genkeypair -alias secure-client2 -keyalg RSA -dname "CN=secure-client2,OU=myorg,O=myorg,L=mycity,S=mystate,C=no" -keypass secret -keystore client2-keystore.jks -storepass secret`

 3. **Import the supported client's public certificates intro the server truststore:**
  - **Export the client1 public certificate**: `keytool -exportcert -alias secure-client1 -file client1-public.cer -keystore client1-keystore.jks -storepass secret`
  - **Export the client2 public certificate**: `keytool -exportcert -alias secure-client2 -file client2-public.cer -keystore client2-keystore.jks -storepass secret`
  - **Import client1 in the server truststore**: `keytool -importcert -keystore server-truststore.jks -alias client1cert -file client1-public.cer -storepass secret`
  - **Import client2 in the server truststore**: `keytool -importcert -keystore server-truststore.jks -alias client2cert -file client2-public.cer -storepass secret`

 4. **Import the server's public certificate into the client truststores:**
   - **Export the server public certificate**: `keytool -exportcert -alias secure-server -file server-public.cer -keystore server-keystore.jks -storepass secret`
   - **Import it in the client1 truststore**: `keytool -importcert -keystore client1-truststore.jks -alias servercert -file server-public.cer -storepass secret` 
   - **Import it in the client2 truststore**: `keytool -importcert -keystore client2-truststore.jks -alias servercert -file server-public.cer -storepass secret` 
  

To access the server `https://localhost:8443` from browser, install client keystore into the browser as a PKCS12 keystore
Create P12 by following command: 

`keytool -importkeystore -srckeystore client1-keystore.jks -destkeystore client1.p12 -deststoretype PKCS12`

`keytool -importkeystore -srckeystore client2-keystore.jks -destkeystore client2.p12 -deststoretype PKCS12`
