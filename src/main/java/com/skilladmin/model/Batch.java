    package com.skilladmin.model;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.fasterxml.jackson.annotation.JsonFormat;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import com.fasterxml.jackson.annotation.JsonManagedReference;
    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.ToString;

    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;

    @Entity
    @Data
    @Table(name = "traingBatch")
    @ToString
    public class Batch {

        @Id
        @GeneratedValue(strategy =  GenerationType.IDENTITY)
        private  Long batchId;

        private  String batchName;

        private  String crationDate;
        private  String duration;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private Date batchStartdate;

        private  String batchEnddate;
        private  double price;


        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name ="tutors_id")
        @JsonBackReference
        private  Tutors tutors;


        @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference

        private List<TraineeAssignment> traineeAssignments = new ArrayList<>();

        @PrePersist
        protected  void create(){
            DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.crationDate= LocalDateTime.now().format(dateTimeFormatter);
        }

    }
