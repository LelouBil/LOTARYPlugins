package fr.leloubil.lotaryitems.Items;

import org.apache.commons.lang.RandomStringUtils;

public class PackID {
    private String First;

    private String sec;

    public PackID() {
        this.First = RandomStringUtils.randomAlphabetic(3).toUpperCase();
        this.sec = RandomStringUtils.randomAlphabetic(5).toUpperCase();
        while (BackPack.getPacks().keySet().contains(this)) {
            this.First = RandomStringUtils.randomAlphabetic(3).toUpperCase();
            this.sec = RandomStringUtils.randomAlphabetic(5).toUpperCase();
        }
    }

    public PackID(String id) {
        this.First = id.split("-")[0];
        this.sec = id.split("-")[1];
    }

    @Override
    public String toString() {
        return First + "-" + sec;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PackID packID = (PackID) o;

        return o.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        int result = First.hashCode();
        result = 31 * result + sec.hashCode();
        return result;
    }
}
