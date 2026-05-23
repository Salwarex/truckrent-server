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

    @OneToMany(mappedBy = "trademark", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Container> containers = new ArrayList<>();


}
