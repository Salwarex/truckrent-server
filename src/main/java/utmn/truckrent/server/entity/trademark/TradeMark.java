package utmn.truckrent.server.entity.trademark;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.container.Container;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trademarks")
public class TradeMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int trademarkId;

    @Column(nullable = false)
    private String title;

//    @OneToMany(mappedBy = "trademark", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<Container> containers = new ArrayList<>();

    public TradeMark(int trademarkId, String title) {
        this.trademarkId = trademarkId;
        this.title = title;
//        this.containers = containers;
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

//    public List<Container> getContainers() {
//        return containers;
//    }
//
//    public void setContainers(List<Container> containers) {
//        this.containers = containers;
//    }
}
