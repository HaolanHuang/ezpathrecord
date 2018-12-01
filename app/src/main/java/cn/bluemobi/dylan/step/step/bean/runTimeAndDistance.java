package cn.bluemobi.dylan.step.step.bean;

import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/*
 create by haolan on 2018/11/21
 */

@Table("runData")
public class runTimeAndDistance {

    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    @Column("today")
    private String today;
    @Column("time")
    private String time;
    @Column("distance")
    private String distance;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToday() { return today; }

    public void setToday(String today) {
        this.today = today;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "StepData{" +
                "id=" + id +
                ", today='" + today + '\'' +
                ", time='" + time + '\'' +
                ", distance=' " + distance +'\'' +
                '}';
    }
}
