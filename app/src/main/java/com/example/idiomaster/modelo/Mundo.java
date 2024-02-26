    package com.example.idiomaster.modelo;

    import java.util.List;

    public class Mundo {
        private String nombre;
        private List<Nivel> niveles;
        private int id;

        public Mundo() {
        }

        public Mundo(String nombre, List<Nivel> niveles) {
            this.nombre = nombre;
            this.niveles = niveles;
        }

        public Mundo(String nombre, List<Nivel> niveles, int id) {
            this.nombre = nombre;
            this.niveles = niveles;
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public List<Nivel> getNiveles() {
            return niveles;
        }

        public void setNiveles(List<Nivel> niveles) {
            this.niveles = niveles;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Mundo{" +
                    "nombre='" + nombre + '\'' +
                    ", niveles=" + niveles +
                    ", id=" + id +
                    '}';
        }
    }
