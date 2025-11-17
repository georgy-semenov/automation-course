package example;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config-${env}.properties"})
public interface StatusConfig extends Config {
    @Key("baseUrl")
    String baseUrl();
}
