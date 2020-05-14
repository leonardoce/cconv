import sqlite3

def calc_id_from_nome(nome):
    nome = nome.strip().replace('(','').replace(')','')
    num = 0
    risultato = ""

    for parola in nome.split(" "):
        if len(parola)>0:
            if num==0:
                risultato += parola.lower()
            else:
                risultato += parola.capitalize()

            num+=1
    
    return risultato

def main():
    conn = sqlite3.connect("origini.db")

    print "<origini>"
    c = conn.cursor()
    c.execute("select nome,  lat_wgs84, lon_wgs84 from catastali")
    for row in c:
        nome = row[0].strip()
        latitude = row[1]
        longitude = row[2]

        print "<origine"
        print "id=\""+calc_id_from_nome(nome)+"\" "
        print "descrizione=\""+nome+"\" "
        print "latitude=\""+latitude+"\" "
        print "longitude=\""+longitude+"\" "
        print "/>"

    c.close()
    conn.close()
    print "</origini>"

if __name__=="__main__":
    main()
