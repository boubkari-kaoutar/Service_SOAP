package com.tp.client;

import com.tp.api.HelloService;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SecureClientDemo {
    public static void main(String[] args) throws Exception {
        URL wsdl = new URL("http://localhost:8080/services/hello-secure?wsdl");
        QName qname = new QName("http://api.cxf.acme.com/", "HelloService");
        Service svc = Service.create(wsdl, qname);
        HelloService port = svc.getPort(HelloService.class);

        Client client = ClientProxy.getClient(port);

        Map<String, Object> outProps = new HashMap<>();
        outProps.put("action", "UsernameToken");
        outProps.put("user", "student");
        outProps.put("passwordType", "PasswordText");
        outProps.put("passwordCallbackRef", new CallbackHandler() {
            @Override
            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                for (Callback cb : callbacks) {
                    if (cb instanceof WSPasswordCallback) {
                        WSPasswordCallback pc = (WSPasswordCallback) cb;
                        pc.setPassword("secret123");
                    }
                }
            }
        });

        WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
        client.getOutInterceptors().add(wssOut);

        System.out.println("=== Test du service sécurisé ===");
        System.out.println(port.sayHello("Secure Client"));
        System.out.println("Person: " + port.findPersonById("P-999").getName());
        System.out.println("✅ Authentification réussie!");
    }
}