package damoa.conf;

import damoa.comm.data.ServerInfo;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;


public class PropertiesLoader {
    private static PropertiesLoader ourInstance = new PropertiesLoader();

    public static PropertiesLoader getInstance() {
        return ourInstance;
    }

    private Configuration configuration;
    private PropertiesLoader() {
        init();
    }

    private void init() {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFileName("application.properties"));
        try {
            configuration = builder.getConfiguration();
            System.out.println("config ....");

            Property.init(configuration);
            ServerInfo.init(configuration);

        } catch (ConfigurationException cex) {
            // loading of the configuration file failed
            cex.printStackTrace();
        }
    }

    public String getConfig(String key) {
        return configuration.getString(key);
    }
}
