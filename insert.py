import psycopg2
from psycopg2 import sql

# Conectar a la base de datos
conn = psycopg2.connect(
    dbname="store",
    user="postgres",
    password="1216",
    host="db",  # Por ejemplo, el nombre del servicio del pod o IP
    port="5432"      # Puerto por defecto de PostgreSQL
)

cursor = conn.cursor()

# Crear una nueva tabla
create_table_query = '''
CREATE TABLE IF NOT EXISTS nueva_tabla (
    id SERIAL PRIMARY KEY,
    data VARCHAR(255)
);
'''
cursor.execute(create_table_query)

# Generar un gran número de filas
rows_to_insert = [(f'Dato {i}',) for i in range(1, 1000001)]  # 1 millón de filas

# Inserción masiva usando ejecutemany
insert_query = sql.SQL("INSERT INTO nueva_tabla (data) VALUES (%s)")
cursor.executemany(insert_query, rows_to_insert)

# Confirmar los cambios
conn.commit()

# Cerrar cursor y conexión
cursor.close()
conn.close()

print("Inserción completada con éxito.")
