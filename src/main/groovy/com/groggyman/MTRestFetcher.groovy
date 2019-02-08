package com.groggyman

import groovyx.net.http.RESTClient

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MTRestFetcher {

    private ExecutorService EXECUTOR_INSTANCE
    private static Runtime rt
    private static long prevTotal = 0
    private static long prevFree

    static main(args){
        rt = Runtime.getRuntime()
        prevFree = rt.freeMemory()

        RestClientService restClientService = new RestClientService()
        MTRestFetcher main = new MTRestFetcher()
//        for(i in 1..200){
//            main.getResourceAsync(restClientService,'getRestResourceStatic', i)
//        }
//        for(i in 1..10){
//            main.getResourceAsync(restClientService,'getRestResource', i, new RESTClient(RestClientService.REST_ENDPOINT))
//        }
        for(i in 1..200){
            main.getResourceAsync(restClientService,'getRestResourceAsync', i)
        }
    }

    def getResourceAsync(RestClientService restClientService, String methodName, int id, RESTClient client = null){
        executor.execute {
            if(client){
                restClientService."${methodName}"(client, id)
            } else {
                def a = restClientService."${methodName}"(id)
                println "a ${a}"
            }
            printMemory(id)
        }
    }

    private static printMemory(int i){
        long total = rt.totalMemory()
        long free = rt.freeMemory()

        println "Calculating memory iteration #${i}"
        if (total != prevTotal || free != prevFree) {
            long used = total - free
            long prevUsed = prevTotal - prevFree
            println "# ${i}, Total: ${total.div(1024)}, Used: ${used.div(1024)}, ∆Used: ${(used - prevUsed).div(1024)}, Free: ${free.div(1024)}, ∆Free: ${(free - prevFree).div(1024)}"
            prevTotal = total
            prevFree = free
        }
    }

    private ExecutorService getExecutor() {
        if (!EXECUTOR_INSTANCE) {
            EXECUTOR_INSTANCE = Executors.newFixedThreadPool(5)
        }
        EXECUTOR_INSTANCE
    }
}
