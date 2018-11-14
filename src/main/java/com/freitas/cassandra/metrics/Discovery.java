package com.freitas.cassandra.metrics;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.management.remote.JMXServiceURL;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

public class Discovery {

    public static class Jvm {
        private final String cassandraInstance;
        private final JMXServiceURL jmxUrl;

        public Jvm(String cassandraInstance, JMXServiceURL jmxUrl) {
            this.cassandraInstance = cassandraInstance;
            this.jmxUrl = jmxUrl;
        }

        public String getCassandraInstance() {
            return cassandraInstance;
        }

        public JMXServiceURL getJmxUrl() {
            return jmxUrl;
        }

        @Override
        public String toString() {
            return "Jvm [cassandraInstance=" + cassandraInstance + ", jmxUrl=" + jmxUrl + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((cassandraInstance == null) ? 0 : cassandraInstance.hashCode());
            result = prime * result + ((jmxUrl == null) ? 0 : jmxUrl.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Jvm other = (Jvm) obj;
            if (cassandraInstance == null) {
                if (other.cassandraInstance != null)
                    return false;
            } else if (!cassandraInstance.equals(other.cassandraInstance))
                return false;
            if (jmxUrl == null) {
                if (other.jmxUrl != null)
                    return false;
            } else if (!jmxUrl.equals(other.jmxUrl))
                return false;
            return true;
        }

    }

    private static final String MAIN_CLASS = "org.apache.cassandra.service.CassandraDaemon";
    private static final String INSTANCE_PROPERTY = "cassandra.instance-id";

    private final Map<String, Jvm> jvms = Maps.newHashMap();

    public Discovery() throws IOException {
        discover();
    }

    public Map<String, Jvm> getJvms() {
        return jvms;
    }

    private void discover() throws IOException {

        for (VirtualMachineDescriptor descriptor : VirtualMachine.list()) {
            VirtualMachine vm;
            try {
                vm = VirtualMachine.attach(descriptor.id());
            }
            catch (AttachNotSupportedException e) {
                throw Throwables.propagate(e);
            }

            if (isCassandra(vm)) {
                String id = cassandraInstanceId(vm);
                if (id == null) {
                    throw new IllegalStateException(String.format(
                            "Cannot determine instance name of process running as PID %s (Hint: missing -D%s=<name>?)",
                            descriptor.id(),
                            INSTANCE_PROPERTY));
                }
                jvms.put(id, new Jvm(id, new JMXServiceURL(connectorAddress(vm))));
            }
        }
    }

    /**
     * Get a connection address for a local management agent. If no such agent exists, one will be
     * started.
     * 
     * @param vm
     *            instance representing a running JVM
     * @return the management agent connection string
     * @throws IOException
     *             if an I/O error occurs while communicating with the VM
     */
    private String connectorAddress(VirtualMachine vm) throws IOException {
        String address = vm.getAgentProperties().getProperty("com.sun.management.jmxremote.localConnectorAddress");
        if (address == null) {
            // No locally connectable agent, install one.
            try {
                String javaHome = vm.getSystemProperties().getProperty("java.home");
                vm.loadAgent(Joiner.on(File.separator).join(javaHome, "lib", "management-agent.jar"));
            }
            catch (AgentLoadException | AgentInitializationException e) {
                throw Throwables.propagate(e);
            }
            address = vm.getAgentProperties().getProperty("com.sun.management.jmxremote.localConnectorAddress");
        }
        return address;
    }

    /** Is this vm running Cassandra? */
    private static boolean isCassandra(VirtualMachine vm) throws IOException {
        if (MAIN_CLASS.equals(vm.getSystemProperties().getProperty("sun.java.command"))) {
            return true;
        }
        return false;
    }

    /** Grok the configured instance ID, or null if unset. */
    private static String cassandraInstanceId(VirtualMachine vm) throws IOException {
        return vm.getSystemProperties().getProperty(INSTANCE_PROPERTY);
    }

    public static void main(String... args) throws IOException {
        for (Jvm j : new Discovery().getJvms().values()) {
            System.err.println(j);
        }
    }
}
