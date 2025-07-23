package gm.zona_fit.controlador;


import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.IClienteServicio;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Data
@ViewScoped
public class IndexControlador {

    @Autowired
    IClienteServicio clienteServicio;
    private List<Cliente> clientes;
    private Cliente clienteSeleccionado;
    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

    @PostConstruct
    public void init(){
        cargarDatos();
    }


    public void cargarDatos(){
        this.clientes = clienteServicio.listarClientes();
        this.clientes.forEach( cliente -> logger.info(cliente.toString()) );
    }



    public void agregarCliente(){
        logger.info("Agregar  ");
        this.clienteSeleccionado = new Cliente();
    }


    public void guardarCliente(){
        logger.info("Ciente a guardar:  " + this.clienteSeleccionado);
        if(this.clienteSeleccionado.getId() == null){
            this.clienteServicio.guardarCliente(this.clienteSeleccionado);
            this.clientes.add(this.clienteSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente agregado"));
        }else{
            logger.info("Ciente a Modificar:  ");
            this.clienteServicio.guardarCliente(this.clienteSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente actualizado"));
        }

        PrimeFaces.current().executeScript("PF('ventanaModalCliente').hide()");
        PrimeFaces.current().ajax().update("forma-clientes:mensajes", "forma-clientes:clientes-tabla");

        this.clienteSeleccionado = null;
    }


    public void eliminarCliente(){
        logger.info("Ciente a eliminar:  " + this.clienteSeleccionado);
        this.clienteServicio.eliminarCliente(this.clienteSeleccionado);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente Eliminado"));
        this.clientes.remove(this.clienteSeleccionado);
        this.clienteSeleccionado = null;
        PrimeFaces.current().ajax().update("forma-clientes:mensajes", "forma-clientes:clientes-tabla");
    }
}
