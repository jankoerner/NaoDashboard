Must-have:
Verbindungen:
- 5 Verbindungen werden in Text Datei gespeichert
- aktuellste wird in IP und Port Textfelder gefüllt
- andere auswählbar in dropdown liste
- Verbindungen werden nicht doppelt gespeichert: auf Duplikate überprüft und entsprechend die Reihenfolge angepasst
- dazu: eigene Überprüfung mit timeout ob die eingegebene Adresse erreichbar ist. Erst dann wird verbunden. 
- connect & disconnect möglich
Laufen:
- Nao läuft gerade aus, zur Seite, nach hinten auf Tastendruck (wasd) oder auf "Button" druck (wasd) 
- kann kurven laufen indem man den slider während der Bewegung bewegt
- Geschwindigkeit regulierbar
- kann sich auch um - 180° bis 180° auf der Stelle drehen
Kopf:
- Nao bewegt seinen Kopf mit ijklm per Tastendruck oder per "Button" druck
- m setzt seinen Kopf dabei in die mitte
LED:
- alle LEDs des Nao ansteuerbar, in Gruppen unterteilt
- alle Farben und an/aus möglich
Sprechen:
- Nao kann in allen verfügbaren Sprachen einen eingegebenen Text sprechen
- Lautstärke, pitch, Geschwindigkeit regulierbar
Audio:
- Audiofiles im Set "Aldebaran" können abgespielt werden
- Abfrage, ob Set existiert. Wenn nicht wird die listview auch nicht angezeigt.
Sensoren:
- beim vorderen Sensor 'dabt' der Nao 
- die hinteren beiden lassen den Nao einen eingegebenen Satz sagen
Batterie:
- Batteriestand wird geladen und am unteren Programmrand angezeigt und ins log geschrieben wenn er unter 5% liegt.
Temperatur:
- wird geladen: Anzeige am unteren Programmrand
- auch Übersicht über Temperatur aller Körperteile vorhanden, Nao liefert aber keine guten Werte (Tab System Information) 
Posen
- alle Posen die der nao hat werden geladen. Es wird ein Bild dazu angezeigt welches die Pose zeigt
- Nao kann aber nicht sit on chair ausführen 

Nice-to-haves:
Tracker:
- Redball und Gesicht Verfolgung ACHTUNG: ROTER NAO VERFOLGT SICH SELBST
- Modi: nur mit Kopf verfolgen/verfolgen (laufen) 
- ein und ausschaltbar
Kamera:
- Kamerabild wird geladen wenn checkbox aktiviert ist, d.h an - ausschaltbar. 
- bei Tests hat das kamerabild sehr gehangen. Wir konnten nicht herausfinden, ob es zb am WLAN oder an den Laptops liegt. Jedenfalls werden Befehle die dem nao gegeben werden während die Kamera aktiv ist evtl sehr verzögert ausgeführt 
- aktuell muss die Kamera deaktiviert werden bevor disconnected wird
Log:
- eigenes log auf der Oberfläche in welches Fehlermeldungen, Infos und Aktionen des Naos geschrieben werden und in verschiedenen Farben hinterlegt werden, je nach Typ der Meldung 
- mit timestamp (timestampformatter:HH:mm:ss) 
Movement Detection:
-  Nao erkennt ob sich was bewegt und sagt dann "Danger, Will Robinson! Danger!" 
- auskommentiert da es noch keine Möglichkeit gibt, diesen Modus abzubrechen und Naos Movement detection mit der Zeit dazu führt, dass das Programm hängt und unbedienbar wird, d.h nicht implementiert. 
GUI:
- In Tabs aufgeteilt:
-> Tab: Connection (verbinde zum nao)
-> Tab: NAO (disabled wenn nicht connected) 
Enthält tabs: Control, Audio, Camera, System Information (...).
Diese Tabs enthalten Boxen/Listen mit Informationen, die vom NAO geladen werden müssen. Damit die Verbindung zum Nao nicht so lange hängt, werden diese Informationen erst beim Wechseln auf den NAO-tab geladen.
Laufen und kopfbewegung durch Tastendruck ist auf allen Tabs möglich.
