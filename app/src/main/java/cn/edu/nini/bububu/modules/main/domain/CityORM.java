package cn.edu.nini.bububu.modules.main.domain;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by HugoXie on 16/7/24.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
@Table("weather_city")
public class CityORM {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private  int id;

    @NotNull
    private  String name;

    public CityORM(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
