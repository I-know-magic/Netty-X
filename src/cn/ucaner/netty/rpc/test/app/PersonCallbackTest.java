/******************************************************************************
* ~ Copyright (c) 2018 [jasonandy@hotmail.com | https://github.com/Jasonandy] *
* ~                                                                           *
* ~ Licensed under the Apache License, Version 2.0 (the "License”);           * 
* ~ you may not use this file except in compliance with the License.          *
* ~ You may obtain a copy of the License at                                   *
* ~                                                                           *
* ~    http://www.apache.org/licenses/LICENSE-2.0                             *
* ~                                                                           *
* ~ Unless required by applicable law or agreed to in writing, software       *
* ~ distributed under the License is distributed on an "AS IS" BASIS,         *
* ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
* ~ See the License for the specific language governing permissions and       *
* ~ limitations under the License.                                            *
******************************************************************************/
package cn.ucaner.netty.rpc.test.app;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import cn.ucaner.netty.rpc.client.AsyncRPCCallback;
import cn.ucaner.netty.rpc.client.RPCFuture;
import cn.ucaner.netty.rpc.client.RpcClient;
import cn.ucaner.netty.rpc.client.proxy.IAsyncObjectProxy;
import cn.ucaner.netty.rpc.registry.ServiceDiscovery;
import cn.ucaner.netty.rpc.test.client.Person;
import cn.ucaner.netty.rpc.test.client.PersonService;

/**
* @Package：cn.ucaner.netty.rpc.test.app   
* @ClassName：PersonCallbackTest   
* @Description：   <p> PersonCallbackTest </p>
* @Author： - luxiaoxun   https://github.com/luxiaoxun/NettyRpc  http://www.importnew.com/24689.html
* @Modify By：   
* @Modify marker：   
* @version    V1.0
 */
public class PersonCallbackTest {
	
    @SuppressWarnings("static-access")
	public static void main(String[] args) {
    	//67.218.158.137:2181 127.0.0.1:8280
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery("67.218.158.137:2181");
        
        final RpcClient rpcClient = new RpcClient(serviceDiscovery);
        
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        try {
            IAsyncObjectProxy client = rpcClient.createAsync(PersonService.class);
            int num = 100;
            
            //String funcName, Object... args ,num
            //RPCFuture helloPersonFuture = client.call("GetTestPerson", "Name", num);
            
            RPCFuture helloPersonFuture = client.call("sayHelloNettyRpc",num);
            helloPersonFuture.addCallback(new AsyncRPCCallback() {
            	
                @SuppressWarnings("unchecked")
				@Override
                public void success(Object result) {
                    List<Person> persons = (List<Person>) result;
                    for (int i = 0; i < persons.size(); ++i) {
                        System.out.println(persons.get(i));
                    }
                    countDownLatch.countDown();
                }

                @Override
                public void fail(Exception e) {
                    System.out.println(e);
                    countDownLatch.countDown();
                }
            });

        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rpcClient.stop();

        System.out.println("End");
    }
}
