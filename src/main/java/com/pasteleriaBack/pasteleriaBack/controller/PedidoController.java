package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.dto.PedidoDTO;
import com.pasteleriaBack.pasteleriaBack.model.Pedido;
import com.pasteleriaBack.pasteleriaBack.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind .annotation .PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

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

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable Integer id, @RequestBody Pedido updatedPedido) {
        return pedidoService.updatePedido(id, updatedPedido);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Integer id) {
        return pedidoService.deletePedido(id);
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