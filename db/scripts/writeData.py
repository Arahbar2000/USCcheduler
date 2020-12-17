import argparse
import requests


departments = ["AHIS", "ALI", "AMST", "ANTH", "ARAB", "ASTR", "BISC", "CHEM", "CLAS", "COLT", "CORE", "CSLC",
               "EALC", "EASC", "ECON", "ENGL", "ENST", "FREN", "GEOG", "GEOL", "GERM", "SWMS", "GR", "HEBR",
               "HIST", "HBIO", "INDS", "IR", "IRAN", "ITAL", "JS", "LAT", "LING", "MATH", "MDA", "MDES", "MPW",
               "NEUR", "NSCI", "OS", "PHED", "PHIL", "PHYS", "POIR", "PORT", "POSC", "PSYC", "REL", "RNR",
               "SLL", "SOCI", "SPAN", "SSCI", "SSEM", "USC", "VISS", "WRIT", "ACCT", "ARCH", "ACAD", "ACCT",
               "BAEP", "BUAD", "BUCO", "DSO", "FBE", "GSBA", "MKT", "MOR", "HRM", "CMPP", "CNTV", "CTAN",
               "CTCS", "CTIN", "CTPR", "CTWR", "IML", "ASCJ", "CMGT", "COMM", "DSM", "JOUR", "PR", "PUBD",
               "DANC", "DENT", "CBY", "DHIS", "THTR", "EDCO", "EDHP", "EDUC", "AME", "ASTE", "BME", "CHE", "CE",
               "CSCI", "EE", "ENE", "ENGR", "ISE", "INF", "ITP", "MASC", "PTE", "SAE", "ART", "CRIT", "DES",
               "FA", "FACE", "FACS", "FADN", "FADW", "FAIN", "FAPH", "FAPT", "FAPR", "FASC", "WCT", "GCT",
               "SCIN", "SCIS", "ARLT", "SI", "ARTS", "HINQ", "SANA", "LIFE", "PSC", "QREA", "GPG", "GPH",
               "GESM", "GERO", "LAW", "ACMD", "ANST", "BIOC", "CBG", "DSR", "HP", "INTD", "MEDB", "MEDS",
               "MICB", "MPHY", "MSS", "NIIN", "PATH", "PHBI", "PM", "PCPA", "SCRM", "ARTL", "MTEC", "MSCR",
               "MTAL", "MUCM", "MUCO", "MUCD", "MUEN", "MUHL", "MUIN", "MUJZ", "MPEM", "MPGU", "MPKS", "MPPM",
               "MPST", "MPVA", "MPWP", "MUSC", "SCOR", "OT", "HCDA", "PHRD", "PMEP", "RXRS", "BKN", "PT",
               "AEST", "HMGT", "MS", "NAUT", "NSC", "PPD", "PPDE", "PLUS", "RED"]



def writeData(department, year, filename):
    f = open(filename, "w")
    session = requests.Session()
    for dept in department:
        try:
            response = session.get(f"https://web-app.usc.edu/web/soc/api/classes/{dept}/{year}/").json()
        except Exception:
            print(f"department {dept} is not found!")
            continue
        # courseID, department, courseNumber, title, startTime, endTime, section, instructors, units, daysOfWeek, currentSpots
        for course in response["OfferedCourses"]["course"]:
            try:
                courseId = course["ScheduledCourseID"]
            except TypeError:
                print(f"no scheduled class in department {dept}!")
                break
            course = course["CourseData"]
            sections = [course["SectionData"]] if type(course["SectionData"]) is not list else course["SectionData"]

            for section in sections:
                if "instructor" in section:
                    instructors = section["instructor"]
                    # multiple instructors
                    if type(instructors) is list:
                        instructor = "&".join([inst["first_name"] + " " + inst["last_name"] for inst in instructors])
                    else:
                        instructor = instructors["first_name"] + " " + instructors["last_name"]
                else:
                    instructor = "TBA"
                f.write(
                    f'''{section["id"]}|{dept}|{courseId.split("-")[-1]}|{section["title"]}|'''
                    f'''{section["start_time"] if "start_time" in section else "TBA"}|'''
                    f'''{section["end_time"] if "end_time" in section else "TBA"}|{section["type"]}|'''
                    f'''{instructor}|{section["units"]}|'''
                    f'''{section["day"] if "day" in section and section["day"] else "TBA"}|'''
                    f'''{section["number_registered"]}/{section["spaces_available"]}\n'''
                )

    f.close()

    # get rid of multiple session at different time
    with open(filename) as f:
        lines = f.readlines()
    with open(filename, "w") as f:
        for line in lines:
            if "[" not in line:
                f.write(line)


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--output", default="/var/lib/mysql-files/courses.csv")
    parser.add_argument("--year", default=20211)
    arg = parser.parse_args()

    writeData(departments, arg.year, arg.output)