package utmn.truckrent.server.entity.trademark;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.container.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "trademarks")
public class TradeMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int trademarkId;

    @Column(nullable = false)
    private String title;

    public TradeMark(int trademarkId, String title) {
        this.trademarkId = trademarkId;
        this.title = title;
    }

    public TradeMark() {
    }

    public int getTrademarkId() {
        return trademarkId;
    }

    public void setTrademarkId(int trademarkId) {
        this.trademarkId = trademarkId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TradeMark tradeMark = (TradeMark) object;
        return trademarkId == tradeMark.trademarkId && Objects.equals(title, tradeMark.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trademarkId, title);
    }
}
