def main():
	for riga in open("origini.txt"):
		riga = riga.strip()
		nome = riga[:riga.find(".")-2].strip()
		coords = riga[riga.find(".")-2:].strip().split(",")

		print "content.clear();"
		print "content.put(\"id\", \"" + nome + "\");"
		print "content.put(\"y\", " + coords[0] + ");"
		print "content.put(\"x\", " + coords[1] + ");"
		print "db.insert(\"origini\", null, content);"
		print ""

main()
