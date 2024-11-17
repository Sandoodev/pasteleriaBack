package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.dto.PedidoDTO;
import com.pasteleriaBack.pasteleriaBack.dto.PedidoClienteDTO;
import com.pasteleriaBack.pasteleriaBack.dto.UpdatePedidoDTO;
import com.pasteleriaBack.pasteleriaBack.model.EstadoPedidoENUM;
import com.pasteleriaBack.pasteleriaBack.model.Pedido;
import com.pasteleriaBack.pasteleriaBack.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind .annotation .PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @CrossOrigin
    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Integer id) {
        return pedidoService.getPedidoById(id);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody PedidoDTO pedidoDTO) {
        return pedidoService.createPedido(pedidoDTO);
    }

    //requerimiento 9: reasignar pedido a cocinero
    @CrossOrigin
    @PutMapping("/reasignar/{pedidoId}")
    public ResponseEntity<Pedido> reasignarPedido(
            @PathVariable Integer pedidoId,
            @RequestParam Integer cocineroDni,
            @RequestParam Integer autorDni) {

        return pedidoService.reasignarPedido(pedidoId, cocineroDni, autorDni);
    }

    //REQUERIMIENTO 17: listado pedidos del cliente
    @CrossOrigin
    @GetMapping("/listadoPedidosCliente")
    public ResponseEntity<List<PedidoClienteDTO>> listarPedidos(
            @RequestParam(required = false) String dniCliente,
            @RequestParam(required = false) String estadoPedido,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {

        // Convierte los parámetros de fecha de String a Timestamp
        Timestamp inicio = null;
        Timestamp fin = null;

        if (fechaInicio != null) {
            try {
                inicio = Timestamp.valueOf(fechaInicio); // Asegúrate de que el formato sea correcto
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null); // Manejar el caso de formato incorrecto
            }
        }

        if (fechaFin != null) {
            try {
                fin = Timestamp.valueOf(fechaFin); // Asegúrate de que el formato sea correcto
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null); // Manejar el caso de formato incorrecto
            }
        }
        // Convertir el estadoPedido de String a EstadoPedidoENUM
        EstadoPedidoENUM estado = null;
        if (estadoPedido != null) {
            try {
                estado = EstadoPedidoENUM.fromString(estadoPedido); // Usar el método de conversión
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null); // Manejar el caso en que el estado no es válido
            }
        }

        // Llamar al servicio para obtener la lista de pedidos
        List<PedidoClienteDTO> pedidos = pedidoService.findPedidosEncargados(inicio, fin, dniCliente, estado);

        return ResponseEntity.ok(pedidos);
    }

    //REQUERIMIENTO 18: actualizacion de pedido
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePedido(
            @PathVariable Integer id,
            @RequestBody UpdatePedidoDTO updatePedidoDTO, // Asegúrate de que este sea el tipo correcto
            @RequestParam Integer autorDni) {
        return pedidoService.updatePedido(id, updatePedidoDTO, autorDni);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(
            @PathVariable Integer id,
            @RequestParam String motivo,
            @RequestParam(required = false) String descripcion,
            @RequestParam Integer dniAutor) { // Agregar dniAutor como parámetro
        return pedidoService.eliminarPedido(id, motivo, descripcion, dniAutor);
    }
    // Otros métodos
     /*
    @CrossOrigin  //IMPORTANTE: es para poder consumir cada endpoint de otra app,(por ejemplo un frontend)
    @GetMapping
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    @CrossOrigin
    @GetMapping("/onemovie/{id}")
    public ResponseEntity<Movie> getOneMovie(@PathVariable Long id){ //con PathVariable le decimos que reciba lo que pusieron en la uri,en este caso lo que ponen en {id}
        Optional<Movie> movie = movieRepository.findById(id); //el finById devuelve un tipo Optional<>, xq puede que lo encuentre o no.
        return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        *//*se usa el método map para devolver una respuesta HTTP 200 OK con la película.
       Si no se encuentra la película, se devuelve una respuesta 404 Not Found con ResponseEntity.notFound().build().*//*
    }

    @CrossOrigin //para que una interfez de terceros(frontend) pueda realizar peticiones
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie *//*este objeto seria el formulario que recibimos del frontend con los datos ya cargados*//*){
        //el @RequestBody es para avisar que el objeto que debe tomar(el formulario) viene en el cuerpo de la peticion, que se recibe mediante el metodo POST cuando se invoca a la pagina
        Movie saveMovie = movieRepository.save(movie);//al save le pasamos el objeto que recibimos como parametro.
        //El save guarda en la base de datos lo que le pasemos como parametro.
        return ResponseEntity.status(HttpStatus.CREATED).body(saveMovie); //devuelvo el estado(.status) de la respuesta(ResponseEntity),y en el cuerpo(body) le inserto el objeto(movie)
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id){ //void porque borramos el elemento
        if(!movieRepository.existsById(id)) { //controlamos si no existe la pelicula con el .existsById(id)
            return ResponseEntity.notFound().build(); //si no la encuentra devolvemos un erro 404
        }
        movieRepository.deleteById(id); //eliminamos la peli
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id,@RequestBody Movie updatedMovie){
        if (!movieRepository.existsById(id)){ //si no esta
            return ResponseEntity.notFound().build(); //devuleve error 404
        }
        updatedMovie.setId(id);
        Movie savedMovie = movieRepository.save(updatedMovie);//la almacenamos para retornarla en el cuerpo del mensaje de OK(linea de abajo).
        return ResponseEntity.ok(savedMovie);
    }

    @CrossOrigin
    @GetMapping("/vote/{id}/{rating}")
    public ResponseEntity<Movie> voteMovie (@PathVariable Long id,@PathVariable double rating){
        if (!movieRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        Optional <Movie> optional = movieRepository.findById(id);
        Movie movie = optional.get();

        double newRating = ((movie.getVotes() * movie.getRating() + rating) / (movie.getVotes() + 1));
        movie.setVotes(movie.getVotes() + 1);
        movie.setRating(newRating);

        Movie savedMovie = movieRepository.save(movie);
        return ResponseEntity.ok(savedMovie);

    }*/
}