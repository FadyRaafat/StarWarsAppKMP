import SwiftUI
import Shared

struct PeopleListView: View {
    let people: [Person]
    let onPersonTap: (Person) -> Void
    @EnvironmentObject var themeManager: ThemeManager
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Header with count
            HStack {
                Text("Found \(people.count) character\(people.count != 1 ? "s" : "")")
                    .font(.title3)
                    .fontWeight(.medium)
                    .foregroundColor(themeManager.currentTheme.primary)
                
                Spacer()
            }
            .padding(.horizontal)
            
            // People List
            ScrollView {
                LazyVStack(spacing: 8) {
                    ForEach(Array(people.enumerated()), id: \.offset) { index, person in
                        PersonItemView(person: person, themeManager: themeManager) {
                            onPersonTap(person)
                        }
                    }
                }
                .padding(.horizontal)
            }
        }
    }
}

struct PersonItemView: View {
    let person: Person
    let themeManager: ThemeManager
    let onTap: () -> Void
    
    var body: some View {
        Button(action: onTap) {
            HStack(spacing: 16) {
                // Character Icon (matching Android design)
                ZStack {
                    RoundedRectangle(cornerRadius: 8)
                        .fill(themeManager.currentTheme.primary.opacity(0.1))
                        .frame(width: 40, height: 40)
                    
                    Image(systemName: "person.fill")
                        .foregroundColor(themeManager.currentTheme.primary)
                        .font(.system(size: 24))
                }
                
                // Character Name
                Text(person.name)
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(.primary)
                    .multilineTextAlignment(.leading)
                    .lineLimit(1)
                
                Spacer()
                
                // Arrow Icon (matching Android)
                Image(systemName: "chevron.right")
                    .foregroundColor(.secondary.opacity(0.6))
                    .font(.system(size: 14, weight: .medium))
            }
            .padding(16)
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(Color(.systemBackground))
                    .shadow(color: Color.black.opacity(0.08), radius: 4, x: 0, y: 2)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(Color(.systemGray5), lineWidth: 0.5)
            )
        }
        .buttonStyle(.plain)
        .scaleEffect(1.0)
        .animation(.easeInOut(duration: 0.1), value: false)
    }
}