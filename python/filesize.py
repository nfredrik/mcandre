#!/usr/bin/env python3

import sys

def getSize(file):
	f = open(file, "rb")

	b = 0

	c = f.read(1)

	while c != "":
		b += 1
		c = f.read(1)

	f.close()

	return b

def usage():
	print("Usage: %s file" % (sys.argv[0]))
	sys.exit()

def main():
	if len(sys.argv) < 2:
		usage()

	size = 0

	try:
		size = getSize(sys.argv[1])
	except Exception:
		raise "Could not open %s" % (sys.argv[1])

	print("%d bytes" % (size))

if __name__ == "__main__":
	main()