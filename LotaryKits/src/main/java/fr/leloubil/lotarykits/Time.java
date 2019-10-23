package fr.leloubil.lotarykits;

import lombok.Getter;

public class Time {
    @Getter
    private Integer days;

    @Getter
    private Integer hours;

    @Getter
    private Integer minutes;

    @Getter
    private Integer secondes;

    public Time(Integer time) {
        Integer secondes = 0;
        Integer minutes = 0;
        Integer heures  = 0;
        Integer jours = 0;
        if(time < 60){
            secondes = time;
        }
        if(time < 3600) {
            minutes = Math.toIntExact(time / 60);
            secondes = time - (minutes * 60);
        }
        if(time < 86400){
            minutes = Math.toIntExact(time / 60);
            heures = Math.toIntExact(minutes / 60);
            secondes = time - (minutes * 60);
            minutes =  minutes - (heures * 60);
        }
        else {
            minutes = Math.toIntExact(time / 60);
            heures = Math.toIntExact(minutes / 60);
            jours = Math.toIntExact(heures / 24);
            secondes = time - (minutes * 60);
            minutes = minutes - (heures * 60);
            heures = heures - (jours * 24);
        }
        this.secondes = secondes;
        this.minutes = minutes;
        this.hours = heures;
        this.days = jours;
    }

    public String SecToString(){
        return secondes + " seconde" + (secondes > 1 ? "s": "");
    }
    public String MinToString(){
        return minutes + " minute" + (minutes > 1 ? "s": "");
    }
    public String HToString(){
        return hours + " heure" + (hours > 1 ? "s": "");
    }
    public String DaysToString(){
        return days + " jour" + (days > 1 ? "s": "");
    }
    @Override
    public String toString() {
        String sec = SecToString();
        String min = MinToString();
        String hour = HToString();
        String days = DaysToString();

        String ret = "";

        if(!days.startsWith("0")){
            ret+= "DAYS";
        }
        if(!hour.startsWith("0")){
            ret+= "HOUR";
        }
        if(!min.startsWith("0")){
            ret+= "MINS";
        }
        if(!sec.startsWith("0")){
            ret+= "SECS";
        }
        if(ret.length() > 4) {
            ret = new StringBuilder(ret).insert(ret.length() - 4, "!!").toString();
        }
        ret = ret.replace("DAYS",days + " ");
        ret = ret.replace("HOUR",hour + " ");
        ret = ret.replace("MINS",min + " ");
        ret = ret.replace("SECS",sec + " ");
        ret = ret.replace("!!","et ");
        ret = ret.substring(0,ret.length() - 1);
        return ret;
    }
}
