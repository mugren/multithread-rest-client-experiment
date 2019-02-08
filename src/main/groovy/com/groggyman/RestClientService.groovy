package com.groggyman

import groovyx.net.http.AsyncHTTPBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient

class RestClientService {

    private static RESTClient STATIC_REST_CLIENT
    private static AsyncHTTPBuilder ASYNC_REST_CLIENT
    static final String REST_ENDPOINT = "https://jsonplaceholder.typicode.com/"

    def getRestResourceStatic(int id){
        try {
            def response = staticClientInstance.get(
                    path: "todos/${id}",
                    contentType: ContentType.JSON
            )
            def responseData = response.data
            if (responseData) {
                println "${responseData}"
            }
            responseData
        } catch (HttpResponseException ex){
            printError(ex, id)
        } catch (Exception ex){
            printError(ex, id)
        }
    }

    def getRestResource(RESTClient client, int id){
        try {
            def response = client.get(
                    path: "todos/${id}",
                    contentType: ContentType.JSON
            )
            def responseData = response.data
            if (responseData) {
                println "${responseData}"
            }
            responseData
        } catch (HttpResponseException ex){
            printError(ex, id)
        } catch (Exception ex){
            printError(ex, id)
        } finally {
            client.shutdown()
        }
    }

    def getRestResourceAsync(int id){
        try {
            def response = asyncClientInstance.get(
                    path: "todos/${id}",
                    contentType: ContentType.JSON
            )
//            ) { resp, data ->
//                println "${data}"
//                return data
//            }
            response.get()
        } catch (HttpResponseException ex){
            printError(ex, id)
        } catch (Exception ex){
            printError(ex, id)
        }
    }

    private static printError(def ex, int id){
        println "ERROR - id ${id}"
        println ex.message
    }

    private static RESTClient getStaticClientInstance() {
        if (!STATIC_REST_CLIENT) {
            STATIC_REST_CLIENT = new RESTClient(uri: REST_ENDPOINT)
        }
        STATIC_REST_CLIENT
    }

    private static AsyncHTTPBuilder getAsyncClientInstance(){
        if (!ASYNC_REST_CLIENT) {
            ASYNC_REST_CLIENT = new AsyncHTTPBuilder(
                    uri: REST_ENDPOINT,
                    poolSize: 5
            )
        }
        ASYNC_REST_CLIENT
    }
}
