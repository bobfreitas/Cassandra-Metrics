/* Copyright 2015 Eric Evans <eevans@wikimedia.org> and Wikimedia Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.freitas.cassandra.metrics;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.junit.Test;

import com.freitas.cassandra.metrics.CarbonConnector;

public class CarbonTest {

    @Test
    public void test() throws IOException {

        final Socket socket = mock(Socket.class);
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        when(socket.getOutputStream()).thenReturn(byteStream);

        CarbonConnector carbon = new CarbonConnector() {
            @Override
            protected Socket createSocket() {
                return socket;
            }
        };

        carbon.write("metric", "value", 1000000);

        assertThat(byteStream.toByteArray(), equalTo("metric value 1000000\n".getBytes()));

        carbon.close();

    }

    @Test(expected = IllegalStateException.class)
    public void testClosed() throws IOException {

        final Socket socket = mock(Socket.class);
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        when(socket.getOutputStream()).thenReturn(byteStream);

        CarbonConnector carbon = new CarbonConnector() {
            @Override
            protected Socket createSocket() {
                return socket;
            }
        };

        carbon.close();
        carbon.write("metric", "value", 1000000);

    }

}
