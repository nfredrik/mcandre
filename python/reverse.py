#!/usr/bin/env python3

import random

def flip(sequence, howmany):
	a = sequence[:howmany]
	a.reverse()

	return a + sequence[howmany:]

def disordered(sequence):
	for i in range(1, len(sequence) - 1):
		if sequence[i - 1] > sequence[i]:
			return True

	return False

if __name__ == "__main__":
	inp = list(range(1, 10))
	numbers = [inp.pop(random.randint(0, len(inp)-1)) for i in range(len(inp))]

	count = 0

	while disordered(numbers):
		print(" ".join([str(e) for e in numbers]))
		howmany = input("How many to flip? ")
		howmany = int(howmany)
		numbers = flip(numbers, howmany)
		count += 1

	print("Done! That took you %d steps." % (count))