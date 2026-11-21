package com.krakedev.proyectos.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fechaLimite;

    @Column(nullable = false)
    private Double costoEstimado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    @JsonIgnoreProperties({"tareas", "hibernateLazyInitializer", "handler"})
    private Proyecto proyecto;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tarea_empleados",
        joinColumns = @JoinColumn(name = "tarea_id"),
        inverseJoinColumns = @JoinColumn(name = "empleado_id")
    )
    @JsonIgnoreProperties({"tareas", "hibernateLazyInitializer", "handler"})
    private List<Empleado> empleados = new ArrayList<>();

    public Tarea() {}

    public Tarea(String descripcion, LocalDate fechaLimite, Double costoEstimado, Proyecto proyecto, List<Empleado> empleados) {
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.costoEstimado = costoEstimado;
        this.proyecto = proyecto;
        this.empleados = empleados != null ? empleados : new ArrayList<>();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }

    public Double getCostoEstimado() { return costoEstimado; }
    public void setCostoEstimado(Double costoEstimado) { this.costoEstimado = costoEstimado; }

    public Proyecto getProyecto() { return proyecto; }
    public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }

    public List<Empleado> getEmpleados() { return empleados; }
    public void setEmpleados(List<Empleado> empleados) { this.empleados = empleados; }
}