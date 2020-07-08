drop database if exists DBHospitalInfectologia2014118;
create database DBHospitalInfectologia2014118;
use DBHospitalInfectologia2014118;

create table Medicos(
	codMedico int not null primary key auto_increment,
	licenciaMedica int (10),
	nombre varchar (100),
	apellidos varchar (100),
	horaEntrada datetime,
	horaSalida datetime,
	turnoMaximo int default 0,
	sexo varchar (20)
);


create table TelefonosMedicos(
	codTelefonoMedico int not null primary key auto_increment,
	telefonoPersonal varchar (15),
	telefonoTrabajo varchar (15),
	codMedico int,
    foreign key (codMedico) references Medicos(codMedico) on delete cascade
); 

create table Horarios(
	codHorario int not null primary key auto_increment,
	horarioInicio datetime,
	horarioSalida datetime,
	lunes tinyint,
    martes tinyint,
    miercoles tinyint,
    jueves tinyint,
    viernes tinyint
);

create table Pacientes(
	codPaciente int not null primary key auto_increment,
	dpi varchar (20),
	apellidos varchar (100),
	nombre varchar (100),
	fechaNacimiento varchar (50),
	edad int default 0,
	direccion varchar (150),
	ocupacion varchar (50),
	sexo varchar (15)
);

create table Especialidades(
	codEspecialidad int not null primary key auto_increment,
	nomEspecialidad varchar (45)
);

create table Areas(
	codArea int not null primary key auto_increment,
	nomArea varchar (45)
);

create table Cargos(
	codCargo int not null primary key auto_increment,
	nomCargo varchar (45)
);

create table ResponsableTurno(
	codResponsableTurno int not null primary key auto_increment,
	nomResponsable varchar (75),
	apellidosResponsable varchar (45),
	telefonoPersonal varchar (10),
	codArea int,
	codCargo int,
    foreign key (codArea) references Areas(codArea)on delete cascade,
    foreign key (codCargo) references Cargos(codCargo)on delete cascade
); 

create table MedicoEspecialidad(
	codMedicoEspecialidad int not null primary key auto_increment,
	codMedico int,
	codHorario int,
    codEspecialidad int,
    foreign key (codEspecialidad) references Especialidades(codEspecialidad)on delete cascade,
    foreign key (codHorario) references Horarios(codHorario)on delete cascade,
    foreign key (codMedico) references Medicos(codMedico)on delete cascade
);

create table Turno(
	codTurno int not null primary key auto_increment,
	fechaTurno date,
	fechaCita date,
	valorCita decimal (10,2),
	codMedicoEspecialidad int,
	codResponsableTurno int,
	codPaciente int,
    foreign key (codMedicoEspecialidad) references MedicoEspecialidad(codMedicoEspecialidad) on delete cascade,
	foreign key (codResponsableTurno) references ResponsableTurno(codResponsableTurno) on delete cascade,
	foreign key (codPaciente) references Pacientes(codPaciente) on delete cascade
);

create table ContactoUrgencia(
	codContactoUrgencia int not null primary key auto_increment,
	nombres varchar (100),
	apellidos varchar (100),
	numContacto varchar (10),
	codPaciente int,
    foreign key (codPaciente) references Pacientes(codPaciente) on delete cascade
);

-- ========================================== TABLAS EXAMEN =======================================================
-- ================================================================================================================

create table TipoUsuario(
	codTipoUsuario int not null primary key auto_increment,
    descripcion varchar(150)
);

create table Estado(
	usuarioEstado tinyint  primary key
);

create table Usuarios(
	codUsuario int not null primary key auto_increment,
    usuarioLogin varchar (45),
    usuarioContrasena varchar (45),
    usuarioEstado tinyint,
    usuarioFecha date,
    usuarioHora time,
    codTipoUsuario int,
    foreign key (codTipoUsuario) references TipoUsuario(codTipoUsuario) on delete cascade,
    foreign key (usuarioEstado) references Estado (usuarioEstado) on delete cascade
);


-- ========================================== PROCEDIMIENTOS ALMACENADOS ==========================================
-- ================================================================================================================
DELIMITER $$
create procedure sp_AgregarMedicos(p_licenciaMedica int, p_nombre varchar(100), p_apellidos varchar(100),p_horaEntrada datetime, p_horaSalida datetime, p_sexo varchar(15))
begin
	insert into Medicos (licenciaMedica, nombre, apellidos, horaEntrada, horaSalida,  sexo)
	values(p_licenciaMedica, p_nombre, p_apellidos, p_horaEntrada, p_horaSalida,  p_sexo);
end$$
delimiter ;
call sp_AgregarMedicos(2054987321, 'Angel', 'Avila', '2019-05-09 07:00:00', '2019-05-09 17:00:00', 'Masculino');
call sp_AgregarMedicos(2058973214, 'Karla', 'Rodas', '2019-05-09 07:00:00', '2019-05-09 17:00:00', 'Femenino');
call sp_AgregarMedicos(1569875362, 'Juan', 'Soria', '2019-05-09 07:00:00', '2019-05-09 17:00:00', 'Masculino');
call sp_AgregarMedicos(1897536985, 'Daniela', 'Alvarado', '2019-05-09 07:00:00', '2019-05-09 17:00:00',  'Femenino');
call sp_AgregarMedicos(2098756325, 'Fernando', 'Hernandez', '2019-05-09 07:00:00', '2019-05-09 17:00:00',  'Masculino');

select * from Medicos;
-- ========================================== PROCEDIMIENTOS Editar ==========================================
-- ================================================================================================================
delimiter $$
create procedure sp_EditarMedicos(p_codMedico int, p_licenciaMedica int, p_nombre varchar(100), p_apellidos varchar(100),p_horaEntrada datetime, p_horaSalida datetime,  p_sexo varchar(15))
begin 
update Medicos
   set  licenciaMedica = p_licenciaMedica, 
   nombre = p_nombre, 
   apellidos = p_apellidos, 
   horaEntrada = p_horaEntrada, 
   horaSalida = p_horaSalida, 
   sexo = p_sexo 
        where codMedico = p_codMedico;
end$$
delimiter ;
call sp_EditarMedicos(1, 1963547896, 'Diego', 'Avila', '2019-05-09 07:00:00', '2019-05-09 17:00:00', 'Masculino');

select * from Medicos;

-- ========================================== PROCEDIMIENTOS editar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_EliminarMedicos(p_codMedico int)
 begin
 delete from Medicos
 where codMedico = p_codMedico;
 end$$
delimiter ;
call sp_EliminarMedicos(5);

select * from Medicos;

-- ========================================== PROCEDIMIENTOS Listar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_ListarMedicos()
 begin
 select M.codMedico as codMedico, licenciaMedica, nombre, apellidos, horaEntrada, horaSalida, turnoMaximo, sexo
 from Medicos M;
 end$$
 delimiter ;
call sp_ListarMedicos();

-- ========================================== PROCEDIMIENTOS Buscar ==========================================
-- ================================================================================================================
DELIMITER $$
create procedure sp_BuscarMedico (in p_codMedico int)
begin
		select codMedico, licenciaMedica, nombre, apellidos, horaEntrada, horaSalida, turnoMaximo, sexo from Medicos
		where p_codMedico = codMedico;
end$$
delimiter ;

call sp_BuscarMedico (1);

-- ========================================== PROCEDIMIENTOS Almacenados ==========================================
-- ================================================================================================================
DELIMITER $$
create procedure sp_AgregarTelefonosMedico(p_telefonoPersonal varchar(15), p_telefonoTrabajo varchar(15), p_codMedico int)
begin
	insert into telefonosMedicos(telefonoPersonal, telefonoTrabajo, codMedico)
    values(p_telefonoPersonal, p_telefonoTrabajo, p_codMedico);
end$$
delimiter ;

call sp_AgregarTelefonosMedico('54789536', '23598756', 1);
call sp_AgregarTelefonosMedico('48793261', '22245987', 2);
call sp_AgregarTelefonosMedico('47856963', '28596354', 3);
call sp_AgregarTelefonosMedico('53698754', '21245798', 4);


-- ========================================== PROCEDIMIENTOS Editar ==========================================
-- ================================================================================================================
DELIMITER $$
create procedure sp_EditarTelefonosMedicos(p_codTelefonoMedico int, p_telefonoPersonal varchar (15), p_telefonoTrabajo varchar (15), p_codMedico int)
begin
		update TelefonosMedicos 
        set telefonoPersonal = p_telefonoPersonal,
		telefonoTrabajo = p_telefonoTrabajo,
        codMedico = p_codMedico
        where codTelefonoMedico = p_codTelefonoMedico;
end$$
delimiter ;
 
call sp_EditarTelefonosMedicos(1, '54236985', '24569863', 4)

-- ========================================== PROCEDIMIENTOS Eliminar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_EliminarTelefonosMedicos(p_codTelefonoMedico int)
 begin
 delete from TelefonosMedicos
 where codTelefonoMedico = p_codTelefonoMedico;
 end$$
delimiter ;

call sp_EliminarTelefonosMedicos(5);

select * from TelefonosMedicos;

-- ========================================== PROCEDIMIENTOS Listar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_ListarTelefonosMedicos()
 begin
 select T.codTelefonoMedico as codTelefonoMedico, telefonoPersonal, telefonoTrabajo, codMedico
 from TelefonosMedicos T;
 end$$
 delimiter ;
 
call sp_ListarTelefonosMedicos();

-- ========================================== PROCEDIMIENTOS Buscar ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_BuscarTelefonoMedico (in p_codTelefonoMedico int)
begin
		select codTelefonoMedico, telefonoPersonal, telefonoTrabajo, codMedico from TelefonosMedicos
		where p_codTelefonoMedico = codTelefonoMedico;
end$$
delimiter ;

call sp_BuscarTelefonoMedico (2);

-- ========================================== PROCEDIMIENTOS ALMACENADOS ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_AgregarHorarios(p_horarioInicio datetime, p_horarioSalida datetime, p_lunes TINYINT, p_martes TINYINT, p_miercoles TINYINT, p_jueves TINYINT, p_viernes TINYINT)
begin
	insert into Horarios(horarioInicio, horarioSalida, lunes, martes, miercoles, jueves, viernes)
    values(p_horarioInicio, p_horarioSalida, p_lunes, p_martes, p_miercoles, p_jueves, p_viernes);
end$$
delimiter ;
 
call sp_AgregarHorarios('2019-05-09 07:00:00', '2019-05-09 17:00:00', 0, 1, 1, 0, 1);
call sp_AgregarHorarios('2019-05-09 07:00:00', '2019-05-09 17:00:00', 0, 1, 0, 0, 1);
call sp_AgregarHorarios('2019-05-09 07:00:00', '2019-05-09 17:00:00', 1, 1, 0, 1, 1);
call sp_AgregarHorarios('2019-05-09 07:00:00', '2019-05-09 17:00:00', 0, 1, 0, 1, 1);
call sp_AgregarHorarios('2019-05-09 07:00:00', '2019-05-09 17:00:00', 1, 0, 0, 1, 0);

-- ========================================== PROCEDIMIENTOS Editar ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_EditarHorarios(p_codHorario int, p_horarioInicio datetime, p_horarioSalida datetime, p_lunes TINYINT, p_martes TINYINT, p_miercoles TINYINT, p_jueves TINYINT, p_viernes TINYINT)
begin
		update Horarios
        set horarioInicio = p_horarioInicio,
		horarioSalida = p_horarioSalida,
        lunes = p_lunes, 
        martes = p_martes, 
        miercoles = p_miercoles, 
        jueves = p_jueves, 
        viernes = p_viernes
        where codHorario = p_codHorario;
end$$
delimiter ;
 
call sp_EditarHorarios(1, '2019-05-09 07:00:00', '2019-05-09 17:00:00', 1, 0, 1, 1, 0)

-- ========================================== PROCEDIMIENTOS Eliminar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_EliminarHorarios(p_codHorario int)
 begin
 delete from Horarios
 where codHorario = p_codHorario;
 end$$
delimiter ;

call sp_EliminarHorarios(5);

select * from Horarios;

-- ========================================== PROCEDIMIENTOS Listar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_ListarHorarios()
 begin
 select H.codHorario as codHorario, horarioInicio, horarioSalida, lunes, martes, miercoles, jueves, viernes
 from Horarios H;
 end$$
 delimiter ;
 
call sp_ListarHorarios();

-- ========================================== PROCEDIMIENTOS Buscar ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_BuscarHorarios(in p_codHorario int)
begin
		select codHorario, horarioInicio, horarioSalida, lunes, martes, miercoles, jueves, viernes from Horarios
		where p_codHorario = codHorario;
end$$
delimiter ;

call sp_BuscarHorarios (2); 
 
-- ========================================== PROCEDIMIENTOS ALMACENADOS ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_AgregarEspecialidades(p_nomEspecialidad varchar(45))
begin
	insert into Especialidades(nomEspecialidad)
    values(p_nomEspecialidad);
end$$
delimiter ;

call sp_AgregarEspecialidades ('Pediatria');
call sp_AgregarEspecialidades ('Infectologia');
call sp_AgregarEspecialidades ('Psiquiatria');
call sp_AgregarEspecialidades ('Rehabilitacion');
call sp_AgregarEspecialidades ('Reumatologia');

-- ========================================== PROCEDIMIENTOS Editar ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_EditarEspecialidades(p_codEspecialidad int, p_nomEspecialidad varchar (45))
begin
		update Especialidades
        set nomEspecialidad = p_nomEspecialidad
        where codEspecialidad = p_codEspecialidad;
end$$
delimiter ;
 
call sp_EditarEspecialidades(1, 'Cirugano');

-- ========================================== PROCEDIMIENTOS Eliminar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_EliminarEspecialidades(p_codEspecialidad int)
 begin
 delete from Especialidades
 where codEspecialidad = p_codEspecialidad;
 end$$
delimiter ;

call sp_EliminarEspecialidades(5);

select * from Especialidades;

-- ========================================== PROCEDIMIENTOS Listar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_ListarEspecialidades()
 begin
 select E.codEspecialidad as codEspecialidad, nomEspecialidad
 from Especialidades E;
 end$$
 delimiter ;
 
call sp_ListarEspecialidades();

-- ========================================== PROCEDIMIENTOS Buscar ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_BuscarEspecialidad (in p_codEspecialidad int)
begin
		select codEspecialidad, nomEspecialidad from Especialidades
		where p_codEspecialidad = codEspecialidad;
end$$
delimiter ;

call sp_BuscarEspecialidad (2); 

-- ========================================== PROCEDIMIENTOS ALMACENADOS ==========================================
-- ================================================================================================================+

DELIMITER $$
create procedure sp_AgregarMedicoEspecialidad(p_codMedico int, p_codEspecialidad int, p_codHorario int)
begin
	insert into MedicoEspecialidad(codMedico, codEspecialidad, codHorario)
    values(p_codMedico, p_codEspecialidad, p_codHorario);
end$$
delimiter ;

call sp_AgregarMedicoEspecialidad (1, 1, 1);
call sp_AgregarMedicoEspecialidad (2, 2, 2);
call sp_AgregarMedicoEspecialidad (3, 3, 3);
call sp_AgregarMedicoEspecialidad (4, 4, 4);


-- ========================================== PROCEDIMIENTOS EDITAR ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_EditarMedicoEspecialidad(p_codMedicoEspecialidad int, p_codMedico int, p_codHorario int, p_codEspecialidad int)
begin
		update MedicoEspecialidad
        set codMedico = p_codMedico,
        codHorario = p_codHorario,
        codEspecialidad = p_codEspecialidad
        where codMedicoEspecialidad = p_codMedicoEspecialidad;
end$$
delimiter ;
 
call sp_EditarMedicoEspecialidad(1, 2, 2, 2);

-- ========================================== PROCEDIMIENTOS Eliminar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_EliminarMedicoEspecialidad(p_codMedicoEspecialidad int)
 begin
 delete from MedicoEspecialidad
 where codMedicoEspecialidad = p_codMedicoEspecialidad;
 end$$
delimiter ;

call sp_EliminarMedicoEspecialidad(5);

select * from MedicoEspecialidad;

-- ========================================== PROCEDIMIENTOS Listar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_ListarMedicoEspecialidad()
 begin
 select M.codMedicoEspecialidad as codMedicoEspecialidad, codMedico, codHorario, codEspecialidad
 from MedicoEspecialidad M;
 end$$
 delimiter ;
 
call sp_ListarHorarios();

-- ========================================== PROCEDIMIENTOS Buscar ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_BuscarMedicoEspecialidad (in p_codMedicoEspecialidad int)
begin
		select codMedicoEspecialidad, codMedico, codHorario, codEspecialidad from MedicoEspecialidad
		where p_codMedicoEspecialidad = codMedicoEspecialidad;
end$$
delimiter ;

call sp_BuscarMedicoEspecialidad (2); 

-- ========================================== PROCEDIMIENTOS ALMACENADOS ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_AgregarPacientes(p_dpi varchar(20),p_apellidos varchar(100), p_nombre varchar(100),  p_fechaNacimiento varchar(50), p_direccion varchar(150), p_ocupacion varchar(150), p_sexo varchar(15))
begin
	insert into Pacientes(dpi,apellidos, nombre,  fechaNacimiento, direccion, ocupacion, sexo)
    values(p_dpi,p_apellidos, p_nombre,  p_fechaNacimiento, p_direccion, p_ocupacion, p_sexo);
end$$
delimiter ;

call sp_AgregarPacientes('3256 35698 0101', 'Velasquez Avila', 'Diego', '1996/02/06',  '3ra calle 6-50 zona6', 'Ingeniero en sistemas', 'Masculino');
call sp_AgregarPacientes('5647 23698 0101', 'Escamilla Toledo', 'Hector', '1991/01/01',  '2da calle 7-75 zona8', 'Juez', 'Masculino');
call sp_AgregarPacientes('2346 78956 0101', 'Castro Reyes', 'Derica', '1999/04/15',  '4ta calle 2-64 zona2', 'Secretaria', 'Femenina');
call sp_AgregarPacientes('5789 63256 0101', 'Castillo Lora ', 'Carlos', '1976/04/23', '5ta calle 3-79 zona3', 'Gerente Empresarial', 'Masculino');
call sp_AgregarPacientes('3256 35698 0101', 'Gonzales Ferrer ', 'Gustavo', '1985/03/20', '7ma calle 4-48 zona12', 'Abogado', 'Masculino');

-- ========================================== PROCEDIMIENTOS Editar ==========================================
-- ================================================================================================================

delimiter $$
create procedure sp_EditarPacientes(p_codPaciente int, p_dpi varchar(20),p_apellidos varchar(100), p_nombre varchar(100),  p_fechaNacimiento varchar (50),  p_direccion varchar(150), p_ocupacion varchar(150), p_sexo varchar(15))
begin 
update Pacientes
   set   dpi = p_dpi, 
   apellidos = p_apellidos, 
   nombre = p_nombre, 
   fechaNacimiento = p_fechaNacimiento, 
   direccion = p_direccion, 
   ocupacion = p_ocupacion, 
   sexo = p_sexo
        where codPaciente = p_codPaciente;
end$$
delimiter ;

call sp_EditarPacientes(1, '4235 56987 0101', 'Lopez Aguilar', 'Estuardo', '1993/03/20', '4ta calle 7-32 zona3', 'Doctor', 'Masculino');

select * from Pacientes;

-- ========================================== PROCEDIMIENTOS Eliminar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_EliminarPacientes(p_codPaciente int)
 begin
 delete from Pacientes
 where codPaciente = p_codPaciente;
 end$$
delimiter ;

call sp_EliminarPacientes(6);

select * from Pacientes;

-- ========================================== PROCEDIMIENTOS Listar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_ListarPacientes()
 begin
 select P.codPaciente as  codPaciente, dpi, apellidos, nombre, fechaNacimiento, edad, direccion, ocupacion, sexo
 from Pacientes P;
 end$$
 delimiter ;
 
call sp_ListarPacientes();

-- ========================================== PROCEDIMIENTOS Buscar ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_BuscarPacientes(p_codPaciente int)
begin
		select codPaciente, dpi, apellidos, nombre, fechaNacimiento, edad, direccion, ocupacion, sexo from Pacientes
		where codPaciente = p_codPaciente;
end$$
delimiter ;

call sp_BuscarPacientes(1)

-- ========================================== PROCEDIMIENTOS ALMACENADOS ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_AgregarContactoUrgencia(p_nombres varchar(100), p_apellidos varchar(100), p_numContacto varchar(10), p_codPaciente int)
begin
	insert into contactoUrgencia(nombres, apellidos, numContacto, codPaciente)
    values(p_nombres, p_apellidos, p_numContacto, p_codPaciente);
end$$
delimiter ;

call sp_AgregarContactoUrgencia ('Alberto', 'Rodriguez', '56988736', 1);
call sp_AgregarContactoUrgencia ('Guillermo', 'Leon', '59632658', 2);
call sp_AgregarContactoUrgencia ('Manuel', 'Albocado', '45693678', 3);
call sp_AgregarContactoUrgencia ('Jose', 'Castillo', '53263956', 4);


-- ========================================== PROCEDIMIENTOS Editar ==========================================
-- ================================================================================================================

delimiter $$
create procedure sp_EditarContactoUrgencia(p_codContactoUrgencia int, p_nombres varchar(100), p_apellidos varchar(100), p_numContacto varchar(10), p_codPaciente int)
begin 
update ContactoUrgencia
   set  nombres = p_nombres,  
   apellidos = p_apellidos, 
   numContacto =p_numContacto, 
   codPaciente = p_codPaciente
        where codContactoUrgencia = p_codContactoUrgencia;
end$$
delimiter ;

call sp_EditarContactoUrgencia(1, 'Victor', 'Poncio', '32435088', 1);

select * from ContactoUrgencia;

-- ========================================== PROCEDIMIENTOS Eliminar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_EliminarContactoUrgencia(p_codContactoUrgencia int)
 begin
 delete from ContactoUrgencia
 where codContactoUrgencia = p_codContactoUrgencia;
 end$$
delimiter ;

call sp_EliminarContactoUrgencia(5);

select * from ContactoUrgencia;

-- ========================================== PROCEDIMIENTOS Listar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_ListarContactoUrgencia()
 begin
 select C.codContactoUrgencia as codContactoUrgencia, nombres, apellidos, numContacto, codPaciente
 from ContactoUrgencia C;
 end$$
 delimiter ;
 
call sp_ListarContactoUrgencia();

-- ========================================== PROCEDIMIENTOS Buscar ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_BuscarContactoUrgencia(p_codContactoUrgencia int)
begin
		select codContactoUrgencia, nombres, apellidos, numContacto, codPaciente from ContactoUrgencia
		where codContactoUrgencia = p_codContactoUrgencia;
end$$
delimiter ;

call sp_BuscarContactoUrgencia(1)

-- ========================================== PROCEDIMIENTOS ALMACENADOS ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_AgregarAreas(p_nomArea varchar(45))
begin
	insert into Areas(nomArea)
    values(p_nomArea);
end$$
delimiter ;

call sp_AgregarAreas ('Infectologia');
call sp_AgregarAreas ('Pediatria');
call sp_AgregarAreas ('Psiquiatria');
call sp_AgregarAreas ('Rehabilitacion');
call sp_AgregarAreas ('Reumatologia');

-- ========================================== PROCEDIMIENTOS Editar ==========================================
-- ================================================================================================================-

delimiter $$
create procedure sp_EditarAreas(p_codArea int, p_nomArea varchar(45))
begin 
update Areas
   set  nomArea = p_nomArea
        where codArea = p_codArea;
end$$
delimiter ;

call sp_EditarAreas(1, 'Cirugia');

select * from Areas;

-- ========================================== PROCEDIMIENTOS Eliminar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_EliminarAreas(p_codArea int)
 begin
 delete from Areas
 where codArea = p_codArea;
 end$$
delimiter ;

call sp_EliminarAreas(5);

select * from Areas;

-- ========================================== PROCEDIMIENTOS Listar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_ListarAreas()
 begin
 select A.codArea as codArea, nomArea
 from Areas A;
 end$$
 delimiter ;
 
call sp_ListarAreas();

-- ========================================== PROCEDIMIENTOS Buscar ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_BuscarAreas(p_codArea int)
begin
		select codArea, nomArea from Areas
		where codArea = p_codArea;
end$$
delimiter ;

call sp_BuscarAreas(1)

-- ========================================== PROCEDIMIENTOS ALMACENADOS ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_AgregarCargos(p_nomCargo varchar(45))
begin
	insert into Cargos(nomCargo)
    values(p_nomCargo);
end$$
delimiter ; 

call sp_AgregarCargos ('Medico');
call sp_AgregarCargos ('Enfermera');
call sp_AgregarCargos ('Medico');
call sp_AgregarCargos ('Enfermera');
call sp_AgregarCargos ('Medico');

-- ========================================== PROCEDIMIENTOS Editar ==========================================
-- ================================================================================================================

delimiter $$
create procedure sp_EditarCargos(p_codCargo int, p_nomCargo varchar(45))
begin 
update Cargos
   set  nomCargo = p_nomCargo
        where codCargo = p_codCargo;
end$$
delimiter ;

call sp_EditarCargos(1, 'Enfermero');

select * from Cargos;

-- ========================================== PROCEDIMIENTOS Eliminar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_EliminarCargos(p_codCargo int)
 begin
 delete from Cargos
 where codCargo = p_codCargo;
 end$$
delimiter ;

call sp_EliminarCargos(5);

select * from Cargos;

-- ========================================== PROCEDIMIENTOS Listar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_ListarCargos()
 begin
 select C.codCargo as codCargo, nomCargo
 from Cargos C;
 end$$
 delimiter ;
 
call sp_ListarCargos();

-- ========================================== PROCEDIMIENTOS Buscar ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_BuscarCargos(p_codCargo int)
begin
		select codCargo, nomCargo from Cargos
		where codCargo = p_codCargo;
end$$
delimiter ;

call sp_BuscarCargos(1)

-- ========================================== PROCEDIMIENTOS ALMACENADOS ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_AgregarResponsableTurno(p_nomResponsable varchar(75), p_apellidosResponsable varchar(45),p_telefonoPersonal varchar(10), p_codArea int, p_codCargo int)
begin
	insert into ResponsableTurno(nomResponsable, apellidosResponsable, telefonoPersonal, codArea, codCargo)
    values(p_nomResponsable, p_apellidosResponsable, p_telefonoPersonal, p_codArea, p_codCargo);
end$$
delimiter ;

call sp_AgregarResponsableTurno ('Diego', 'Avila', '54789536', 1, 1);
call sp_AgregarResponsableTurno ('Karla', 'Rodas', '48793261', 2, 2);
call sp_AgregarResponsableTurno ('Juan', 'Soria', '47856963', 3, 3);
call sp_AgregarResponsableTurno ('Luisa', 'Leon', '53698754', 4, 4);

-- ========================================== PROCEDIMIENTOS Editar ==========================================
-- ================================================================================================================


delimiter $$
create procedure sp_EditarResponsableTurno(p_codResponsableTurno int, p_nomResponsable varchar(75), p_apellidosResponsable varchar(45),p_telefonoPersonal varchar(10), p_codArea int, p_codCargo int)
begin 
update ResponsableTurno
   set  nomResponsable = p_nomResponsable,
   apellidosResponsable = p_apellidosResponsable,
   telefonoPersonal = p_telefonoPersonal, 
   codArea = p_codArea, 
   codCargo = p_codCargo
        where codResponsableTurno = p_codResponsableTurno;
end$$
delimiter ;

call sp_EditarResponsableTurno(1, 'Julio', 'Castillo', '53236987', 1, 1);

select * from ResponsableTurno;

-- ========================================== PROCEDIMIENTOS Eliminar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_EliminarResponsableTurno(p_codResponsableTurno int)
 begin
 delete from ResponsableTurno
 where codResponsableTurno = p_codResponsableTurno;
 end$$
delimiter ;

call sp_EliminarResponsableTurno(5);

select * from ResponsableTurno;

-- ========================================== PROCEDIMIENTOS Listar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_ListarResponsableTurno()
 begin
 select R.codResponsableTurno as codResponsableTurno, nomResponsable, apellidosResponsable, telefonoPersonal, codArea, codCargo
 from ResponsableTurno R;
 end$$
 delimiter ;
 
call sp_ListarResponsableTurno();

-- ========================================== PROCEDIMIENTOS Buscar ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_BuscarResponsableTurno(p_codResponsableTurno int)
begin
		select codResponsableTurno, nomResponsable, apellidosResponsable, telefonoPersonal, codArea, codCargo from ResponsableTurno
		where codResponsableTurno = p_codResponsableTurno;
end$$
delimiter ;

call sp_BuscarResponsableTurno(1)

-- ========================================== PROCEDIMIENTOS ALMACENADOS ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_AgregarTurno(p_fechaTurno DATE, p_fechaCita DATE, p_valorCita decimal(10,2), p_codMedicoEspecialidad int, p_codResponsableTurno int, p_codPaciente int)
begin
	insert into Turno(fechaTurno, fechaCita, valorCita, codMedicoEspecialidad, codResponsableTurno, codPaciente)
    values(p_fechaTurno, p_fechaCita, p_valorCita, p_codMedicoEspecialidad, p_codResponsableTurno, p_codPaciente);
end$$ 
delimiter ;

call sp_AgregarTurno ('2019/03/23', '2019/03/26', 50.50, 1, 1, 1);
call sp_AgregarTurno ('2019/02/05', '2019/02/10', 45.25, 2, 2, 2);
call sp_AgregarTurno ('2019/04/12', '2019/04/13', 90.80, 3, 3, 3);
call sp_AgregarTurno ('2019/05/20', '2019/03/24', 50.50, 4, 4, 4);


-- ========================================== PROCEDIMIENTOS Editar ==========================================
-- ================================================================================================================

delimiter $$
create procedure sp_EditarTurno(p_codTurno int, p_fechaTurno DATE, p_fechaCita DATE, p_valorCita decimal(10,2), p_codMedicoEspecialidad int, p_codResponsableTurno int, p_codPaciente int)
begin 
update Turno
   set  fechaTurno = p_fechaTurno, 
   fechaCita = p_fechaCita, 
   valorCita = p_valorCita, 
   codMedicoEspecialidad = p_codMedicoEspecialidad, 
   codResponsableTurno = p_codResponsableTurno, 
   codPaciente = p_codPaciente
        where codTurno = p_codTurno;
end$$
delimiter ;

call sp_EditarTurno(1, '2019/05/10', '2019/05/12', 45.25, 1, 1, 1);

select * from Turno;

-- ========================================== PROCEDIMIENTOS Eliminar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_EliminarTurno(p_codTurno int)
 begin
 delete from Turno
 where codTurno = p_codTurno;
 end$$
delimiter ;

call sp_EliminarTurno(5);

select * from Turno;

-- ========================================== PROCEDIMIENTOS Listar ==========================================
-- ================================================================================================================

delimiter $$
 create procedure sp_ListarTurno()
 begin
	select T.codTurno as codTurno, fechaTurno, fechaCita, valorCita, codMedicoEspecialidad, codResponsableTurno, codPaciente
	from Turno T;
 end$$
 delimiter ;
 
call sp_ListarTurno();

-- ========================================== PROCEDIMIENTOS Buscar ==========================================
-- ================================================================================================================

DELIMITER $$
create procedure sp_BuscarTurno(p_codTurno int)
begin
		select codTurno, fechaTurno, fechaCita, valorCita, codMedicoEspecialidad, codResponsableTurno, codPaciente from Turno
		where codTurno = p_codTurno;
end$$
delimiter ;

call sp_BuscarTurno(1);

-- ========================================== PROCEDIMIENTOS EXAMEN =======================================================
-- ========================================================================================================================

-- ========================================== PROCEDIMIENTOS TIPOUSUARIO =====================================================
-- ========================================================================================================================

delimiter $$
create procedure sp_AgregarTipoUsuario(p_descripcion varchar (150))
begin
	insert into TipoUsuario(descripcion)
    values (p_descripcion);
end $$
delimiter ;

call sp_AgregarTipoUsuario('Administrador');
call sp_AgregarTipoUsuario('Root');
call sp_AgregarTipoUsuario('Invitado');
call sp_AgregarTipoUsuario('Invitado');

delimiter $$
create procedure sp_EditarTipoUsuario(p_codTipoUsuario int, p_descripcion varchar(150))
begin
	update TipoUsuario
    set descripcion = p_descripcion
    where codTipoUsuario = p_codTipoUsuario;
end $$
delimiter ;

call sp_EditarTipoUsuario(3,'Invitado');

delimiter $$
create procedure sp_EliminarTipoUsuario(p_codTipoUsuario int)
begin
	delete from TipoUsuario
    where codTipoUsuario = p_codTipoUsuario;
end $$
delimiter ;

call sp_EliminarTipoUsuario(4);

delimiter $$
create procedure sp_ListarTipoUsuario()
begin
	select TU.codTipoUsuario as codTipoUsuario,descripcion 
	from TipoUsuario TU;
end $$
delimiter ;

call sp_ListarTipoUsuario();

delimiter $$
create procedure sp_BuscarTipoUsuario(p_codTipoUsuario int)
begin
	select codTipoUsuario, descripcion from TipoUsuario
    where codTipoUsuario = p_codTipoUsuario;
end $$
delimiter ;

call sp_BuscarTipoUsuario(1);

-- ========================================== PROCEDIMIENTOS USUARIOS =====================================================
-- ========================================================================================================================

delimiter $$
create procedure sp_AgregarEstado(p_usuarioEstado tinyint)
begin
	insert into Estado(usuarioEstado)
    values (p_usuarioEstado);
end $$
delimiter ;

call sp_AgregarEstado(true);
call sp_AgregarEstado(false);

delimiter $$
create procedure sp_EditarEstado(p_usuarioEstado tinyint)
begin
	update Estado
    set usuarioEstado = p_usuarioEstado
    where usuarioEstado = p_usuarioEstado;
end $$
delimiter ;

call sp_EditarEstado(true);
call sp_EditarEstado(false);

select * from Estado;

delimiter $$
create procedure sp_EliminarEstado(p_usuarioEstado tinyint)
begin
	delete from Estado
    where usuarioEstado = p_usuarioEstado;
end $$
delimiter ;

call sp_EliminarEstado (true);

delimiter $$
create procedure sp_ListarEstado()
begin
	select E.usuarioEstado as usuarioEstado 
    from Estado E;
end $$
delimiter ;

call sp_ListarEstado();

delimiter $$
create procedure sp_BuscarEstado(p_usuarioEstado tinyint)
begin
	select usuarioEstado from Estad
    where usuarioEstado = p_usuarioEstado;
end $$
delimiter ;
call sp_BuscarEstado(true);

-- ========================================== PROCEDIMIENTOS USUARIOS =====================================================
-- ========================================================================================================================



delimiter $$
create procedure sp_AgregarUsuarios(p_usuarioLogin varchar(45), p_usuarioContrasena varchar (45), p_usuarioEstado tinyint, p_usuarioFecha date, p_usuarioHora time, p_codTipoUsuario int)
begin 
	insert into Usuarios(usuarioLogin, usuarioContrasena, usuarioEstado, usuarioFecha, usuarioHora, codTipoUsuario)
    values (p_usuarioLogin, p_usuarioContrasena, p_usuarioEstado, p_usuarioFecha, p_usuarioHora, p_codTipoUsuario);
end $$
delimiter ;

call sp_AgregarUsuarios ("Diego", "Contrasena", true, '2019/07/10','12:00:00',1);
call sp_AgregarUsuarios ("Pedro", "123", false, '2019/07/25','13:00:00',3);
call sp_AgregarUsuarios ("Andres", "456", false, '2019/07/24','14:00:00',3);
call sp_AgregarUsuarios ("Gerardo", "789", false, '2019/07/25','13:00:00',3);
call sp_AgregarUsuarios ("Juan Jose", "741", false, '2019/07/26','17:00:00',3);

select * from Usuarios;

delimiter $$
create procedure sp_EditarUsuarios(p_codUsuario int, p_usuarioLogin varchar (45), p_usuarioContrasena varchar (45), p_usuarioEstado tinyint, p_usuarioFecha date, p_usuarioHora time,p_codTipoUsuario int)
begin
	update Usuarios
    set usuarioLogin = p_usuarioLogin,
    usuarioContrasena = p_usuarioContrasena,
    usuarioEstado = p_usuarioEstado,
    usuarioFecha = p_usuarioFecha,
    usuarioHora = p_usuarioHora,
    codTipoUsuario = p_codTipoUsuario
    where codUsuario = p_codUsuario;
end $$
delimiter ;

call sp_EditarUsuarios(1,"DAV4","Programacion2014118",true,'2019/07/25','14:00:00',1);

delimiter $$
create procedure sp_EliminarUsuarios(p_codUsuario int)
begin
	delete from Usuarios
    where codUsuario = p_codUsuario;
end $$
delimiter ;

call sp_EliminarUsuarios (5);

delimiter $$
create procedure sp_ListarUsuarios()
begin
	select U.codUsuario as codUsuario, usuarioLogin, usuarioContrasena, usuarioEstado, usuarioFecha, usuarioHora, codTipoUsuario
	from Usuarios u;
end $$
delimiter ;

call sp_ListarUsuarios();

delimiter $$
create procedure sp_BuscarUsuarios(p_codUsuario int)
begin
	select codUsuario, usuarioLogin, usuarioContrasena, usuarioEstado, usuarioFecha, usuarioHora, codTipoUsuario from Usuarios
    where codUsuario = p_codUsuario;
end $$
delimiter ;

call sp_BuscarUsuarios(1);

-- ========================================== TRIGGERS  ===========================================================
-- ================================================================================================================

delimiter $$
Create trigger tr_Pacientes 
	before insert on Pacientes
    for each row 
    Begin
    
    set new.edad = timestampdiff(year, new.fechaNacimiento, now());
    
    end$$  
delimiter ;


delimiter $$
Create trigger tr_Pacientes_Update
	before update on Pacientes
    for each row 
    Begin
    
    set new.edad = timestampdiff(year, new.fechaNacimiento, now());
    
    end $$
delimiter ; 


delimiter $$
create trigger tr_Turno_Insert
before insert on MedicoEspecialidad
for each row 
begin
		Declare suma int;
			select sum(Horarios.lunes + Horarios.martes + Horarios.miercoles + Horarios.jueves + Horarios.viernes)
			into suma From Horarios 
            where codHorario = new.codHorario;
            update Medicos 
            set turnoMaximo = suma 
            where codMedico = new.codMedico;
end$$
delimiter ;

	
delimiter $$
create trigger tr_Turno_Update
before update on MedicoEspecialidad
for each row
begin
		Declare suma int;
			select sum(Horarios.lunes + Horarios.martes + Horarios.miercoles + Horarios.jueves + Horarios.viernes)
			into suma From Horarios 
            where codHorario = new.codHorario;
            update Medicos 
            set turnoMaximo = suma 
            where codMedico = new.codMedico;
end $$
delimiter ;

Alter User 'root'@'localhost' IDENTIFIED WITH mysql_native_password by 'admin';