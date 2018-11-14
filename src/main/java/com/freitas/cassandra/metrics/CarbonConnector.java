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

import static com.freitas.cassandra.metrics.Constants.DEFAULT_GRAPHITE_HOST;
import static com.freitas.cassandra.metrics.Constants.DEFAULT_GRAPHITE_PORT;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.google.common.base.Charsets;

public class CarbonConnector implements AutoCloseable {
    private final String hostname;
    private final int port;

    private Socket socket;
    private OutputStream outStream;
    private boolean isClosed = false;

    public CarbonConnector() throws CarbonException {
        this(DEFAULT_GRAPHITE_HOST);
    }

    public CarbonConnector(String host) throws CarbonException {
        this(host, DEFAULT_GRAPHITE_PORT);
    }

    public CarbonConnector(String host, int port) throws CarbonException {
        this.hostname = checkNotNull(host, "host argument");
        checkArgument((port > 0 && port < Short.MAX_VALUE), "port argument");
        this.port = port;

        try {
            this.socket = createSocket();
            this.outStream = this.socket.getOutputStream();
        } catch (IOException e) {
            throw new CarbonException(String.format("Connecting to %s:%s: %s", host, port, e.getMessage()), e);
        }
    }

    public void write(Object metric, Object value) throws CarbonException {
        write(metric, value, System.currentTimeMillis() / 1000);
    }

    public void write(Object metric, Object value, Number timestamp) throws CarbonException {
        checkState(!this.isClosed, "cannot write to closed object");

        try {
            this.outStream.write(format(metric, value, timestamp));
        }
        catch (IOException e) {
            throw new CarbonException(String.format("Writing to %s:%s: %s", this.hostname, this.port, e.getLocalizedMessage()), e);
        }
    }

    private byte[] format(Object metric, Object value, Number timestamp) {
        return String.format("%s %s %d%n", metric, value, timestamp).getBytes(Charsets.UTF_8);
    }

    protected Socket createSocket() throws IOException {
        return new Socket(this.hostname, this.port);
    }

    @Override
    public void close() throws IOException {
        this.outStream.close();
        this.socket.close();
        this.isClosed = true;
    }

    @Override
    public String toString() {
        return "Carbon [hostname=" + hostname + ", port=" + port + ", isClosed=" + isClosed + "]";
    }

}
