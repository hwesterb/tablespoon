import pdb, subprocess, json, os, sys, collections, signal
from installer import Installer


def exit_handler(signal, frame):
	with open('data.json', "w") as outfile:
		json.dump(d, outfile)
		print 'Converting to Json and quitting program.'
	sys.exit()	

signal.signal(signal.SIGINT, exit_handler)

if not Installer().install_pkg('collectl'):
	print 'Terminating the program, could not install collectl.'
	sys.exit()

cmd = ["collectl", "-P"]

p = subprocess.Popen(cmd,stdout=subprocess.PIPE,stderr=subprocess.STDOUT)

d = collections.OrderedDict()

def build_dictionary(headers):
	headers_list = headers.split(' ')
	for h in headers_list:
		d[h] = []

def add_to_dictionary(row):
	row_list = row.split(' ')
	for values, row_item in zip(d.values(), row_list):
		values.append(row_item)

for line in iter(p.stdout.readline, b''):
	current = line.rstrip()
	if not bool(d): #true if dict empty
		if current[:1] is '#':
			build_dictionary(current[1:])
	else:
		add_to_dictionary(current)

