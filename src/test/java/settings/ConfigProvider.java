package settings;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public interface ConfigProvider {

    static Config readConfig() { return ConfigFactory.load("application.conf"); }

    String URL = readConfig().getString("url");

    Integer POST_CODE_LEN = readConfig().getInt("post_code_len");

    String DEFAULT_LAST_NAME = readConfig().getString("customers.pavel_last_name");

    String LOWER_LAST_NAME = readConfig().getString("customers.artem_last_name");

    String EMPTY_LAST_NAME = readConfig().getString("customers.empty_last_name");

    String INCORRECT_LAST_NAME = readConfig().getString("customers.incorrect_last_name");

    String CIRILLIC_LAST_NAME = readConfig().getString("customers.cyrillic_last_name");
}
