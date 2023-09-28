package com.bank.service.impl;

import com.bank.dto.CustomerDTO;
import com.bank.entity.Customer;
import com.bank.entity.Movement;
import com.bank.repository.CustomerRepository;
import com.bank.service.CustomerService;
import com.bank.utility.BCrypt;
import com.bank.utility.exceptions.NoEncontrado;
import com.bank.utility.exceptions.YaExiste;
import com.bank.utility.mapper.ClienteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

  private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
  private final CustomerRepository clienteRepository;
  private final ClienteMapper clienteMapper;

  @Autowired
  public CustomerServiceImpl(
      CustomerRepository clienteRepository,
      ClienteMapper clienteMapper) {
    this.clienteRepository = clienteRepository;
    this.clienteMapper = clienteMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CustomerDTO> getClientes() {
    List<Customer> clientes = clienteRepository.findAll();
    return clientes.stream().map(clienteMapper::clienteToClienteDTO).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerDTO getCliente(Long clienteId) {
    return clienteRepository.findById(clienteId).map(clienteMapper::clienteToClienteDTO)
        .orElse(null);
  }

  @Override
  @Transactional
  public CustomerDTO createCliente(CustomerDTO clienteDTO) {
    Optional<Customer> clienteOpt = clienteRepository.findByIdentificacion(
        clienteDTO.getIdentificacion());
    if (clienteOpt.isPresent()) {
      logger.warn("Cliente ya existe");
      throw new YaExiste("Cliente ya existe");
    }
    Customer cliente = clienteMapper.clienteDTOToCliente(clienteDTO);
    cliente.setPassword(BCrypt.hashpw(cliente.getPassword(), BCrypt.gensalt()));
    final Customer clienteCreated = clienteRepository.save(cliente);
    logger.info("Cliente creado!");
    return clienteMapper.clienteToClienteDTO(clienteCreated);
  }

  @Override
  @Transactional
  public void deleteById(Long clienteId) {
    clienteRepository.findById(clienteId)
        .orElseThrow(() -> new NoEncontrado("Cliente no encontrado"));
    clienteRepository.deleteById(clienteId);
    logger.info("Cliente eliminado!");
  }

  @Override
  @Transactional
  public CustomerDTO updateCliente(Long clienteId, CustomerDTO clienteDTO) {
    Optional<Customer> clienteOpt = clienteRepository.findById(clienteId);
    if (clienteOpt.isPresent()) {
      Customer cliente = clienteMapper.clienteDTOToCliente(clienteDTO);
      Customer clienteActualizado = clienteOpt.get();
      clienteActualizado.setNombre(cliente.getNombre());
      clienteActualizado.setGenero(cliente.getGenero());
      clienteActualizado.setEdad(cliente.getEdad());
      clienteActualizado.setIdentificacion(cliente.getIdentificacion());
      clienteActualizado.setDireccion(cliente.getDireccion());
      clienteActualizado.setTelefono(cliente.getTelefono());
      clienteActualizado.setPassword(BCrypt.hashpw(cliente.getPassword(), BCrypt.gensalt()));
      clienteActualizado.setEstado(cliente.getEstado().booleanValue());
      clienteActualizado.setCuentas(cliente.getCuentas());
      clienteRepository.save(clienteActualizado);
      logger.info("Cliente actualizado!");
      return clienteMapper.clienteToClienteDTO(clienteActualizado);
    } else {
      logger.warn("Cliente no encontrado");
      throw new NoEncontrado("Cliente no encontrado");
    }
  }

  @Override
  @Transactional
  public CustomerDTO actualizacionParcialByFields(Long clienteId, Map<String, Object> fields) {
    Optional<Customer> clienteOpt = clienteRepository.findById(clienteId);
    if (clienteOpt.isPresent()) {
      fields.forEach((key, value) -> {
        if (key.equals("genero")) {
          value = clienteMapper.stringToGenero(value.toString());
        }
        if (key.equals("estado")) {
          value = clienteMapper.stringToBoolean(value.toString());
        }
        Field field = ReflectionUtils.findField(Customer.class, key);
        field.setAccessible(true);
        ReflectionUtils.setField(field, clienteOpt.get(), value);
      });
      clienteOpt.get()
          .setPassword(BCrypt.hashpw(clienteOpt.get().getPassword(), BCrypt.gensalt()));
      Customer cliente = clienteRepository.save(clienteOpt.get());
      CustomerDTO clienteDTO = clienteMapper.clienteToClienteDTO(cliente);
      logger.info("Cliente parcialmente actualizado!");
      return clienteDTO;
    } else {
      logger.warn("Cliente no encontrado");
      throw new NoEncontrado("Cliente no encontrado");
    }
  }

  @Override
  public Customer getMovByClienteId(Long clienteId, LocalDate fechaInicial,
                                    LocalDate fechaFinal) {
    final Optional<Customer> clienteOpt = clienteRepository.findById(clienteId);
    if (clienteOpt.isPresent()) {
      Customer finalC = clienteOpt.get();
      finalC.getCuentas().forEach(c -> {
        List<Movement> movimientoList = c.getMovimientos().stream()
            .filter(m -> m.getFecha().isEqual(fechaInicial) || (m.getFecha().isAfter(fechaInicial)
                && m.getFecha().isBefore(fechaFinal)) || m.getFecha().isEqual(fechaFinal))
            .toList();
        c.setMovimientos(movimientoList);
      });
      logger.info("Listado de movimientos obtenido!");
      return finalC;
    } else {
      logger.warn("Cliente no encontrado");
      throw new NoEncontrado("Cliente no encontrado");
    }
  }
}